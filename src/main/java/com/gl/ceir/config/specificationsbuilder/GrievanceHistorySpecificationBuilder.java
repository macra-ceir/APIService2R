package com.gl.ceir.config.specificationsbuilder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Expression;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import com.gl.ceir.config.controller.GrievanceController;
import com.gl.ceir.config.model.app.GrievanceHistory;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.util.DbFunctions;

public class GrievanceHistorySpecificationBuilder {
	private static final Logger logger = LogManager.getLogger(GrievanceController.class);

	private final List<SearchCriteria> params;
	private final String dialect;
	

	public GrievanceHistorySpecificationBuilder(String dialect) {
		params = new ArrayList<>();
		this.dialect = dialect;
	}

	public final GrievanceHistorySpecificationBuilder with(SearchCriteria criteria) { 
		params.add(criteria);
		return this;
	}

	public Specification<GrievanceHistory> build() { 
		// convert each of SearchCriteria params to Specification and construct combined specification based on custom rules.
		Specification<GrievanceHistory> finalSpecification = null;
		List<Specification<GrievanceHistory>> specifications = createSpecifications();
		if(!specifications.isEmpty()) {
			finalSpecification = Specification.where(specifications.get(0));
			for(int i = 1; i<specifications.size() ;i++) {
				finalSpecification = finalSpecification.and(specifications.get(i));
			}
		}
		return finalSpecification;
	}

	private List<Specification<GrievanceHistory>> createSpecifications(){
		List<Specification<GrievanceHistory>> specifications = new ArrayList<Specification<GrievanceHistory>>();
		// Path<Tuple> tuple = null;
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
					return cb.greaterThan(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
				}
				else if(SearchOperation.LESS_THAN.equals(searchCriteria.getSearchOperation())
						&& Datatype.DATE.equals(searchCriteria.getDatatype())){
					Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
					return cb.lessThan(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
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
}
