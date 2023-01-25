package springbook.issuetracker.sqlservice;

import org.springframework.context.annotation.Import;

import springbook.config.SqlServiceContext;

@Import(value = SqlServiceContext.class)
public @interface EnableSqlService {

}
