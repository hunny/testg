package hh.learng.cloud.tm

class InformationAboutPostgreSQL {

	static main(args) {
		def query = Gdbc.source(['host': '192.168.9.205', 'name': 'cloudDachser_Dev']);
		try {
			query.eachRow(types()) {
				println it
			}
			query.eachRow(users()) {
				println it
			}
			query.eachRow(databases()) {
				println it
			}
			query.eachRow(schemas()) {
				println it
			}
			query.eachRow(tables()) {
				println it
			}
			query.eachRow(fields()) {
				println it
			}
			query.eachRow(constraints()) {
				println it
			}
			query.eachRow(tablespaces()) {
				println it
			}
			query.eachRow(views()) {
				println it
			}
		} finally {
		  query.close();
		}
	}
	
	/**
	 * Get Types list
	 * @return
	 */
	static def types() {
		println 'Get Types list'
		def sql = """
			SELECT oid, format_type(oid, NULL) AS typname FROM pg_type WHERE typtype = 'b'
		"""
		return sql
	}
	
	/**
	 * Get users list
	 * @return
	 */
	static def users() {
		println 'Get users list'
		def sql = """
			SELECT rolname FROM pg_roles WHERE rolcanlogin ORDER BY 1
		"""
		return sql
	}
	
	/**
	 * Get databases list by name of schema
	 * @return
	 */
	static def databases(schema) {
		println 'Get databases list by name of schema:'
		def condition = schema ? "n.nspname = '${schema}' AND" : ''
		def sql = """
			SELECT n.nspname as "Schema", c.relname as datname, CASE c.relkind 
			WHEN 'r' THEN 'table'  
			WHEN 'v' THEN 'view' 
			WHEN 'i' THEN 'index' 
			WHEN 'S' THEN 'sequence'  
			WHEN 's' THEN 'special' 
			END as "Type", u.usename as "Owner" 
			FROM pg_catalog.pg_class c 
			LEFT JOIN pg_catalog.pg_user u ON u.usesysid = c.relowner 
			LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace 
			WHERE 1 = 1 AND ${condition} c.relkind IN ('r','')  
			AND n.nspname NOT IN ('pg_catalog', 'pg_toast', 'information_schema') 
		"""
		return sql.toString()
	}
	
	/**
	 * How to get a list of schemes
	 * @return
	 */
	static def schemas() {
		def sql = """
			SELECT 
			CASE 
			    WHEN nspname LIKE E'pg_temp_%' THEN 1 
			    WHEN (nspname LIKE E'pg_%') THEN 0  
			    ELSE 3 
			END AS nsptyp, nsp.nspname, nsp.oid, pg_get_userbyid(nspowner) 
			    AS namespaceowner, 
			    nspacl, description,  
			    has_schema_privilege(nsp.oid, 'CREATE') as cancreate 
			FROM pg_namespace nsp 
			LEFT OUTER JOIN pg_description des ON des.objoid=nsp.oid  
			WHERE NOT ((nspname = 'pg_catalog' AND EXISTS 
			(SELECT 1 FROM pg_class 
			    WHERE relname = 'pg_class' 
			    AND relnamespace = nsp.oid LIMIT 1)) OR  
			(nspname = 'information_schema' AND 
			    EXISTS (SELECT 1 FROM pg_class 
			            WHERE relname = 'tables' 
			            AND relnamespace = nsp.oid LIMIT 1)) OR  
			(nspname LIKE '_%' AND 
			    EXISTS (SELECT 1 FROM pg_proc 
			            WHERE proname='slonyversion' 
			            AND pronamespace = nsp.oid LIMIT 1)) OR  
			(nspname = 'dbo' AND 
			    EXISTS (SELECT 1 FROM pg_class 
			            WHERE relname = 'systables' 
			            AND relnamespace = nsp.oid LIMIT 1)) OR  
			(nspname = 'sys' AND 
			    EXISTS (SELECT 1 FROM pg_class 
			            WHERE relname = 'all_tables' 
			            AND relnamespace = nsp.oid LIMIT 1))
			) 
			AND nspname NOT LIKE E'pg_temp_%'
			AND nspname NOT LIKE E'pg_toast_temp_%' 
			ORDER BY 1, nspname
		"""
		return sql
	}
	
	/**
	 * Get all tables for schema "public"
	 * @param table
	 * @return
	 */
	static def tables(schema) {
		println 'Get all tables for schema'
		def condition = schema ? "n.nspname = '${schema}' AND" : ''
		def sql = """
			SELECT n.nspname as "Schema",  c.relname AS datname,  
			CASE c.relkind 
			    WHEN 'r' THEN 'table' 
			    WHEN 'v' THEN 'view' 
			    WHEN 'i' THEN 'index' 
			    WHEN 'S' THEN 'sequence' 
			    WHEN 's' THEN 'special' 
			END as "Type",  u.usename as "Owner", 
			(SELECT obj_description(c.oid, 'pg_class')) AS comment  
			FROM pg_catalog.pg_class c 
			LEFT JOIN pg_catalog.pg_user u ON u.usesysid = c.relowner 
			LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace 
			WHERE 1 = 1 AND ${condition} c.relkind IN ('r','') 
			AND n.nspname NOT IN ('pg_catalog', 'pg_toast', 'information_schema')
			ORDER BY datname ASC 
		"""
		return sql.toString()
	}
	
	/**
	 * Get all fields for table "table1", with additional information 
	 * (type, default value, not null flag, length, comment, foreign key name, primary key name)
	 * @param table
	 * @return
	 */
	static def fields(table) {
		def condition = table ? "AND tablename = '${table}'" : ''
		def sql = """
			SELECT pg_tables.tablename, pg_attribute.attname AS field, 
			    format_type(pg_attribute.atttypid, NULL) AS "type", 
			    pg_attribute.atttypmod AS len,
			    (SELECT col_description(pg_attribute.attrelid, 
			            pg_attribute.attnum)) AS comment, 
			    CASE pg_attribute.attnotnull 
			        WHEN false THEN 1  ELSE 0  
			    END AS "notnull", 
			    pg_constraint.conname AS "key", pc2.conname AS ckey, 
			    (SELECT pg_attrdef.adsrc FROM pg_attrdef 
			        WHERE pg_attrdef.adrelid = pg_class.oid 
			        AND pg_attrdef.adnum = pg_attribute.attnum) AS def 
			FROM pg_tables, pg_class 
			JOIN pg_attribute ON pg_class.oid = pg_attribute.attrelid 
			    AND pg_attribute.attnum > 0 
			LEFT JOIN pg_constraint ON pg_constraint.contype = 'p'::"char" 
			    AND pg_constraint.conrelid = pg_class.oid AND
			    (pg_attribute.attnum = ANY (pg_constraint.conkey)) 
			LEFT JOIN pg_constraint AS pc2 ON pc2.contype = 'f'::"char" 
			    AND pc2.conrelid = pg_class.oid 
			    AND (pg_attribute.attnum = ANY (pc2.conkey)) 
			WHERE pg_class.relname = pg_tables.tablename  
			    AND pg_tables.tableowner = "current_user"() 
			    AND pg_attribute.atttypid <> 0::oid  
			    ${condition} 
			ORDER BY field ASC 
		"""
		return sql.toString()
	}
	
	/**
	 * Get constraints list for current database
	 * @return
	 */
	static def constraints() {
		println 'Get constraints list for current database'
		def sql = """
			SELECT DISTINCT (pc2.relname || '.' || r.conname) AS fullname, 
			    r.conname AS constraint_name,
			    r.contype AS constraint_type,
			    r.condeferrable AS is_deferrable,
			    r.condeferred AS is_deferred,
			    r.confupdtype AS update_action,
			    r.confdeltype AS delete_action,
			    pc1.relname AS foreign_table,
			    pc2.relname AS this_table,
			    kcu1.constraint_schema AS this_schema,
			    pg_catalog.pg_get_constraintdef(r.oid, true) as sqlstr 
			FROM pg_constraint AS r
			LEFT JOIN pg_class AS pc1 ON pc1.oid = r.confrelid
			LEFT JOIN pg_class AS pc2 ON pc2.oid = r.conrelid
			LEFT JOIN information_schema.key_column_usage AS kcu1 ON 
			(kcu1.table_name=pc2.relname AND kcu1.constraint_name=r.conname)
			ORDER BY 1;
		"""
		return sql.toString()
	}
	
	/**
	 * Get tablespaces list
	 * @return
	 */
	static def tablespaces() {
		println 'Get tablespaces list'
		def sql = """
			SELECT spcname AS name FROM pg_tablespace ORDER BY spcname ASC
		"""
		return sql.toString()
	}
	
	/**
	 * Get all views for schema public with sql code
	 * @return
	 */
	static def views(schema) {
		def condition = schema ? "AND n.nspname='${schema}'" : ''
		def sql = """
			SELECT c.oid, c.xmin, c.relname, pg_get_userbyid(c.relowner) AS viewowner,  
			c.relacl, description, pg_get_viewdef(c.oid, true) AS code 
			FROM pg_class c 
			LEFT OUTER JOIN pg_description des ON (des.objoid=c.oid and des.objsubid=0) 
			LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace  
			WHERE (
			    (c.relhasrules AND (EXISTS ( 
			        SELECT r.rulename FROM pg_rewrite r 
			        WHERE ( (r.ev_class = c.oid) AND (bpchar(r.ev_type) = '1'::bpchar))
			        ))
			    )
			    OR (c.relkind = 'v'::char)) 
			    AND n.nspname='public' 
			ORDER BY relname ASC
		"""
		return sql.toString()
	}

}
