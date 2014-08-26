package liquibase.ext.ora.materializedview.droplog;

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
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;

import org.junit.Before;
import org.junit.Test;

public class DropMaterializedViewLogTest extends BaseTestCase {

    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/materializedview/droplog/changelog.test.xml";
        connectToDB();
        cleanDB();
    }

    @Test
    public void getChangeMetaData() {
        DropMaterializedViewLogChange createMaterializedViewChange = new DropMaterializedViewLogChange();

        assertEquals("dropMaterializedViewLog", ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getName());
        assertEquals("Drop materialized view log", ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getDescription());
        assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(createMaterializedViewChange).getPriority());
    }

    @Test
    public void getConfirmationMessage() {
        DropMaterializedViewLogChange change = new DropMaterializedViewLogChange();
        change.setTableName("MVLOG_TABLE");

        assertEquals("Materialized view log on MVLOG_TABLE has been dropped", change.getConfirmationMessage());
    }

    @Test
    public void generateStatement() {

        DropMaterializedViewLogChange change = new DropMaterializedViewLogChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setTableName("MVLOG_TABLE");

        Database database = new OracleDatabase();
        SqlStatement[] sqlStatements = change.generateStatements(database);

        assertEquals(1, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof DropMaterializedViewLogStatement);

        assertEquals("schemaName not copied", "SCHEMA_NAME", ((DropMaterializedViewLogStatement) sqlStatements[0]).getSchemaName());
        assertEquals("tableName not copied", "MVLOG_TABLE", ((DropMaterializedViewLogStatement) sqlStatements[0]).getTableName());
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
        assertEquals( "number of changesets in the " + changeLogFile + " is incorrect", 3, changeSets.size() );
        ChangeSet changeSet = changeSets.get(2);

        // Test CreateMaterializedViewTest3
        assertEquals("Wrong number of changes found in changeset CreateMaterializedViewTest3", 1, changeSet.getChanges().size());
        Change change = changeSet.getChanges().get(0);

        List<String> expectedQueries = new ArrayList<String>();
        expectedQueries.add("DROP MATERIALIZED VIEW LOG ON MVLOG_TABLE");

        Sql[] sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
        assertEquals( "wrong number of statements generated", expectedQueries.size(), sql.length );
        assertEquals( "SQL Incorrect", expectedQueries.get(0), sql[0].toSql());
    }

    @Test
    public void test() throws Exception {
        liquiBase.update(new Contexts());
    }
}
