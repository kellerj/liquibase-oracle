package liquibase.ext.ora.materializedview.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import liquibase.Contexts;
import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.change.ChangeMetaData;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.materializedview.create.CreateMaterializedViewChange;
import liquibase.ext.ora.materializedview.create.CreateMaterializedViewStatement;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;

import org.junit.Before;
import org.junit.Test;

public class CreateMaterializedViewTest extends BaseTestCase {

    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/creatematerializedview/changelog.test.xml";
        connectToDB();
        cleanDB();
    }

    @Test
    public void getChangeMetaData() {
        CreateMaterializedViewChange createMaterializedViewChange = new CreateMaterializedViewChange();

        assertEquals("createMaterializedView", ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getName());
        assertEquals("Create materialized view", ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getDescription());
        assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getPriority());
    }

    @Test
    public void getConfirmationMessage() {
        CreateMaterializedViewChange change = new CreateMaterializedViewChange();
        change.setViewName("VIEW_NAME");

        assertEquals("Materialized view VIEW_NAME has been created", "Materialized view " + change.getViewName()
                + " has been created");
    }

    @Test
    public void generateStatement() {

        CreateMaterializedViewChange change = new CreateMaterializedViewChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setViewName("VIEW_NAME");
        change.setColumnAliases("COLUMN_ALIASES");
        change.setObjectType("OBJECT_TYPE");
        change.setTableSpace("TABLE_SPACE");
        change.setQueryRewrite(true);
        change.setSubquery("SUBQUERY");

        change.setReducedPrecision(true);
        change.setUsingIndex(true);
        change.setForUpdate(true);

        Database database = new OracleDatabase();
        SqlStatement[] sqlStatements = change.generateStatements(database);

        assertEquals(1, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof CreateMaterializedViewStatement);

        assertEquals("SCHEMA_NAME", ((CreateMaterializedViewStatement) sqlStatements[0]).getSchemaName());
        assertEquals("VIEW_NAME", ((CreateMaterializedViewStatement) sqlStatements[0]).getViewName());
        assertEquals("COLUMN_ALIASES", ((CreateMaterializedViewStatement) sqlStatements[0]).getColumnAliases());
        assertEquals("OBJECT_TYPE", ((CreateMaterializedViewStatement) sqlStatements[0]).getObjectType());
        assertEquals("TABLE_SPACE", ((CreateMaterializedViewStatement) sqlStatements[0]).getTableSpace());
        assertEquals(Boolean.TRUE, ((CreateMaterializedViewStatement) sqlStatements[0]).getQueryRewrite());
        assertEquals("SUBQUERY", ((CreateMaterializedViewStatement) sqlStatements[0]).getSubquery());

        assertTrue(((CreateMaterializedViewStatement) sqlStatements[0]).getReducedPrecision());
        assertTrue(((CreateMaterializedViewStatement) sqlStatements[0]).getUsingIndex());
        assertTrue(((CreateMaterializedViewStatement) sqlStatements[0]).getForUpdate());
    }

    @Test
    public void parseAndGenerate() throws Exception {
        Database database = liquiBase.getDatabase();
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();

        ChangeLogParameters changeLogParameters = new ChangeLogParameters();


        DatabaseChangeLog changeLog = ChangeLogParserFactory.getInstance().getParser(changeLogFile, resourceAccessor).parse(changeLogFile,
                changeLogParameters, resourceAccessor);

        liquiBase.checkLiquibaseTables( false, changeLog, new Contexts() );
        changeLog.validate(database);

        List<ChangeSet> changeSets = changeLog.getChangeSets();
        assertEquals( "number of changesets in the " + changeLogFile + " is incorrect", 7, changeSets.size() );
        ChangeSet changeSet = changeSets.get(4);

        // Test CreateMaterializedViewTest5
        assertEquals("Wrong number of changes found in changeset CreateMaterializedViewTest5", 1, changeSet.getChanges().size());
        Change change = changeSet.getChanges().get(0);

        List<String> expectedQueries = new ArrayList<String>();
        expectedQueries.add("CREATE MATERIALIZED VIEW zuiolView REFRESH ON DEMAND WITH PRIMARY KEY AS select * from Table1");

        Sql[] sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
        assertEquals( "wrong number of statements generated", expectedQueries.size(), sql.length );
        assertEquals( " SQL Incorrect", expectedQueries.get(0), sql[0].toSql());

        // Test CreateMaterializedViewTest6
        expectedQueries.clear();
        expectedQueries.add("CREATE MATERIALIZED VIEW zuiolTable ON PREBUILT TABLE REFRESH ON DEMAND WITH PRIMARY KEY AS select * from Table1");
        changeSet = changeSets.get(5);
        assertEquals("Wrong number of changes found in changeset CreateMaterializedViewTest5", 1, changeSet.getChanges().size());
        change = changeSet.getChanges().get(0);

        sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
        assertEquals( "wrong number of statements generated", expectedQueries.size(), sql.length );
        assertEquals(expectedQueries.get(0), sql[0].toSql());

        // Test CreateMaterializedViewTest7
        expectedQueries.clear();
        expectedQueries.add("CREATE MATERIALIZED VIEW zuiolView2 BUILD DEFERRED REFRESH ON COMMIT FORCE WITH ROWID ENABLE QUERY REWRITE AS select * from Table1");
        changeSet = changeSets.get(6);
        assertEquals("Wrong number of changes found in changeset CreateMaterializedViewTest5", 1, changeSet.getChanges().size());
        change = changeSet.getChanges().get(0);

        sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
        assertEquals( "wrong number of statements generated", expectedQueries.size(), sql.length );
        assertEquals(expectedQueries.get(0), sql[0].toSql());
    }

    @Test
    public void test() throws Exception {
        liquiBase.update(new Contexts());
        liquiBase.rollback(1, new Contexts());
    }
}
