package liquibase.ext.ora.materializedview.createlog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.change.ChangeMetaData;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;

import org.junit.Before;
import org.junit.Test;

public class CreateMaterializedViewLogTest extends BaseTestCase {

    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/materializedview/createlog/changelog.test.xml";
        connectToDB();
        cleanDB();
    }

    @Test
    public void getChangeMetaData() {
        CreateMaterializedViewLogChange createMaterializedViewChange = new CreateMaterializedViewLogChange();

        assertEquals("createMaterializedViewLog", ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getName());
        assertEquals("Create materialized view log", ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getDescription());
        assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getPriority());
    }

    @Test
    public void getConfirmationMessage() {
        CreateMaterializedViewLogChange change = new CreateMaterializedViewLogChange();
        change.setTableName("MVLOG_TABLE");

        assertEquals("Materialized view log on MVLOG_TABLE has been created", change.getConfirmationMessage());
    }

    @Test
    public void generateStatement() {

        CreateMaterializedViewLogChange change = new CreateMaterializedViewLogChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setTableName("MVLOG_TABLE");
        change.setTableSpace("TABLE_SPACE");
        change.setRefreshWithRowid(true);

        Database database = new OracleDatabase();
        SqlStatement[] sqlStatements = change.generateStatements(database);

        assertEquals(1, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof CreateMaterializedViewLogStatement);

        assertEquals("schemaName not copied", "SCHEMA_NAME", ((CreateMaterializedViewLogStatement) sqlStatements[0]).getSchemaName());
        assertEquals("tableName not copied", "MVLOG_TABLE", ((CreateMaterializedViewLogStatement) sqlStatements[0]).getTableName());
        assertEquals("tableSpace not copied", "TABLE_SPACE", ((CreateMaterializedViewLogStatement) sqlStatements[0]).getTableSpace());
        assertEquals("refreshWithRowid not copied", Boolean.TRUE, ((CreateMaterializedViewLogStatement) sqlStatements[0]).getRefreshWithRowid());
    }

    @Test
    public void parseAndGenerate() throws Exception {
        Database database = liquiBase.getDatabase();
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();

        ChangeLogParameters changeLogParameters = new ChangeLogParameters();

        DatabaseChangeLog changeLog = ChangeLogParserFactory.getInstance().getParser(changeLogFile, resourceAccessor).parse(changeLogFile,
                changeLogParameters, resourceAccessor);

        liquiBase.checkLiquibaseTables( false, changeLog, new Contexts(), new LabelExpression() );
        changeLog.validate(database);

        List<ChangeSet> changeSets = changeLog.getChangeSets();
        assertEquals( "number of changesets in the " + changeLogFile + " is incorrect", 3, changeSets.size() );
        ChangeSet changeSet = changeSets.get(2);

        // Test CreateMaterializedViewTest5
        assertEquals("Wrong number of changes found in changeset CreateMaterializedViewTest3", 2, changeSet.getChanges().size());
        Change change = changeSet.getChanges().get(0);

        List<String> expectedQueries = new ArrayList<String>();
        expectedQueries.add("CREATE MATERIALIZED VIEW LOG ON MVLOG_TABLE WITH PRIMARY KEY");

        Sql[] sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
        assertEquals( "wrong number of statements generated", expectedQueries.size(), sql.length );
        assertEquals( "SQL Incorrect", expectedQueries.get(0), sql[0].toSql());

        change = changeSet.getChanges().get(1);

        expectedQueries.clear();
        expectedQueries.add("CREATE MATERIALIZED VIEW LOG ON MVLOG_TABLE_2 WITH ROWID");

        sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
        assertEquals( "wrong number of statements generated", expectedQueries.size(), sql.length );
        assertEquals( "SQL Incorrect", expectedQueries.get(0), sql[0].toSql());

    }

    @Test
    public void test() throws Exception {
        liquiBase.update(new Contexts());
        liquiBase.rollback(1, (String) null);
    }
}
