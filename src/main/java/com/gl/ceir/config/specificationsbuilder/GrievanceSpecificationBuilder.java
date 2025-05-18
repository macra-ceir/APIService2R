package com.gl.ceir.config.specificationsbuilder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import com.gl.ceir.config.model.app.Grievance;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.util.DbFunctions;

public class GrievanceSpecificationBuilder {
	private static final Logger logger = LogManager.getLogger(GrievanceSpecificationBuilder.class);

	private final List<SearchCriteria> params;
	private final List<SearchCriteria> searchParams;
	private final String dialect;
	

	public GrievanceSpecificationBuilder(String dialect) {
		params = new ArrayList<>();
		searchParams = new ArrayList<>();
		this.dialect = dialect;
	}

	public final GrievanceSpecificationBuilder with(SearchCriteria criteria) { 
		params.add(criteria);
		return this;
	}
	
	public final GrievanceSpecificationBuilder orSearch(SearchCriteria criteria) { 
		searchParams.add(criteria);
		return this;
	}

	public Specification<Grievance> build() { 
		// convert each of SearchCriteria params to Specification and construct combined specification based on custom rules.
		Specification<Grievance> finalSpecification   = null;
		Specification<Grievance> searchSpecification  = null;
		List<Specification<Grievance>> specifications = createSpecifications( params );
		if(!specifications.isEmpty()) {
			finalSpecification = Specification.where(specifications.get(0));
			for(int i = 1; i<specifications.size() ;i++) {
				finalSpecification = finalSpecification.and(specifications.get(i));
			}
		}
		if( !searchParams.isEmpty() ) {
			specifications = createSpecifications( searchParams );
			if( !specifications.isEmpty()) {
				/*searchSpecification = ((root,query,cb)->{ return cb.or(cb.like(root.get(searchParams.get(0).getKey()), "%"+(String)searchParams.get(0).getValue()+"%"),
						cb.like(root.get(searchParams.get(1).getKey()), "%"+(String)searchParams.get(1).getValue()+"%"));
					});*/
				//searchSpecification = ((root,query,cb)->{ return cb.or(getSearchPredicates(root, cb, searchParams));});
				searchSpecification = specifications.get(0);
				for(int i = 1; i<specifications.size() ;i++) {
					searchSpecification = searchSpecification.or(specifications.get(i));
				}
				finalSpecification = finalSpecification.and( searchSpecification );
			}
		}
		return finalSpecification;
	}

	private List<Specification<Grievance>> createSpecifications( List<SearchCriteria> criterias){
		List<Specification<Grievance>> specifications = new ArrayList<Specification<Grievance>>();
		// Path<Tuple> tuple = null;
		try {
			for(SearchCriteria searchCriteria : criterias) {
				specifications.add((root, query, cb)-> {
					// Path<Tuple> tuple = root.<Tuple>get(searchCriteria);
					if(SearchOperation.GREATER_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.greaterThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.LESS_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.lessThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.EQUALITY.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.EQUALITY.equals(searchCriteria.getSearchOperation())
							&& Datatype.INT.equals(searchCriteria.getDatatype())) {
						return cb.equal(root.get(searchCriteria.getKey()), (Integer)searchCriteria.getValue());
					}
					else if(SearchOperation.EQUALITY.equals(searchCriteria.getSearchOperation())
							&& Datatype.LONG.equals(searchCriteria.getDatatype())) {
						return cb.equal(root.get(searchCriteria.getKey()), (Long)searchCriteria.getValue());
					}
					else if(SearchOperation.GREATER_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.DATE.equals(searchCriteria.getDatatype())){
						Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
						return cb.greaterThanOrEqualTo(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.LESS_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.DATE.equals(searchCriteria.getDatatype())){
						Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
						return cb.lessThanOrEqualTo(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.NEGATION.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.NEGATION.equals(searchCriteria.getSearchOperation())
							&& Datatype.INT.equals(searchCriteria.getDatatype())) {
						return cb.notEqual(root.get(searchCriteria.getKey()), (Integer)searchCriteria.getValue());
					}else if(SearchOperation.NEGATION.equals(searchCriteria.getSearchOperation())
							&& Datatype.LONG.equals(searchCriteria.getDatatype())) {
						return cb.notEqual(root.get(searchCriteria.getKey()), (Long)searchCriteria.getValue());
					}else if(SearchOperation.LIKE.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.like(root.get(searchCriteria.getKey()), "%"+(String)searchCriteria.getValue()+"%");
					}else {
						return null;
					}
				});
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
		return specifications;
	}
	
	private Predicate[] getSearchPredicates(Root<Grievance> root, CriteriaBuilder cb, List<SearchCriteria> criterias ){
		int i = 0;
		Predicate[] list = new Predicate[criterias.size()];
		try {
			for(SearchCriteria searchCriteria : criterias) {
				list[i] = cb.like(root.get(searchCriteria.getKey()), "%"+(String)searchCriteria.getValue()+"%");
				i++;
			}
		}catch( Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return list;
	}
	
}
