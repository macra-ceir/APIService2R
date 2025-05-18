package com.gl.ceir.config.specificationsbuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;

import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.app.TypeApprovedDb;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.util.DbFunctions;




public class TypeApprovedSpecificationBuilder {
	private final List<SearchCriteria> params;
	private final List<SearchCriteria> searchParams;
	private final String dialect;
	private List<Specification<TypeApprovedDb>> specifications;


	public TypeApprovedSpecificationBuilder(String dialect) {
		params = new ArrayList<>();
		specifications = new LinkedList<>();
		searchParams = new ArrayList<>();
		this.dialect = dialect;
	}

	public final TypeApprovedSpecificationBuilder with(SearchCriteria criteria) { 
		params.add(criteria);
		return this;
	}

	public final TypeApprovedSpecificationBuilder orSearch(SearchCriteria criteria) { 
		searchParams.add(criteria);
		return this;
	}
	
	public Specification<TypeApprovedDb> build() { 

		Specification<TypeApprovedDb> finalSpecification = null;
		Specification<TypeApprovedDb> searchSpecification  = null;

		List<Specification<TypeApprovedDb>> specifications = createSpecifications( params );
		if(!specifications.isEmpty()) {
			finalSpecification = Specification.where(specifications.get(0));
			for(int i = 1; i<specifications.size() ;i++) {
				finalSpecification = finalSpecification.and(specifications.get(i));
			}
		}
		if( !searchParams.isEmpty() ) {
			specifications = createSpecifications( searchParams );
			if( !specifications.isEmpty()) {
				searchSpecification = specifications.get(0);
				for(int i = 1; i<specifications.size() ;i++) {
					searchSpecification = searchSpecification.or(specifications.get(i));
				}
				finalSpecification = finalSpecification.and( searchSpecification );
			}
		}

		return finalSpecification;
	}

	private List<Specification<TypeApprovedDb>> createSpecifications( List<SearchCriteria> criterias ){

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
			e.printStackTrace();
		}

		return specifications;
	}
}
