package com.gl.ceir.config.specificationsbuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import com.gl.ceir.config.model.app.ConsignmentMgmt;
import com.gl.ceir.config.model.app.Grievance;
import com.gl.ceir.config.model.app.Notification;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.util.DbFunctions;

public class NotificationSpecificationBuilder {

	private static final Logger logger = LogManager.getLogger(NotificationSpecificationBuilder.class);

	private final List<SearchCriteria> params;
	private final List<SearchCriteria> searchParams;
	private final String dialect;
	private List<Specification<Notification>> specifications;

	public NotificationSpecificationBuilder(String dialect) {
		params = new ArrayList<>();
		searchParams = new ArrayList<>();
		specifications = new LinkedList<>();
		this.dialect = dialect;
	}

	public final NotificationSpecificationBuilder with(SearchCriteria criteria) { 
		params.add(criteria);
		return this;
	}

	public final NotificationSpecificationBuilder orSearch(SearchCriteria criteria) { 
		searchParams.add(criteria);
		return this;
	}

	public Specification<Notification> build() { 
		// convert each of SearchCriteria params to Specification and construct combined specification based on custom rules.

		Specification<Notification> finalSpecification = null;

		Specification<Notification> searchSpecification  = null;
		List<Specification<Notification>> specifications = createSpecifications( params );

		if(!specifications.isEmpty()) {
			finalSpecification = Specification.where(specifications.get(0));

			for(int i = 1; i<specifications.size(); i++) {
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

	public void addSpecification(Specification<Notification> specification) { 
		specifications.add(specification);
	}

	private List<Specification<Notification>> createSpecifications(List<SearchCriteria> criterias){

		try {
			for(SearchCriteria searchCriteria : params) {
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
					else if(SearchOperation.GREATER_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.DATE.equals(searchCriteria.getDatatype())){
						Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
						return cb.greaterThan(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.LESS_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.DATE.equals(searchCriteria.getDatatype())){
						Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
						return cb.lessThan(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
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

	public Specification<ConsignmentMgmt> in(SearchCriteria searchCriteria, List<Integer> status){
		return (root, query, cb) -> {
			logger.info("In query save ");
			return cb.in(root.get(searchCriteria.getKey())).value(status);
		};
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
