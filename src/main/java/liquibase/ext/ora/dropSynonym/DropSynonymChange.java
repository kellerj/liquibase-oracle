package liquibase.ext.ora.dropSynonym;

import java.text.MessageFormat;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "dropSynonym", description = "Drop synonym", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class DropSynonymChange extends AbstractChange {

	private String synonymSchemaName;
	private String synonymName;
	private Boolean force = Boolean.FALSE;
	private Boolean isPublic = Boolean.FALSE;

	public String getSynonymName() {
		return synonymName;
	}

	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}

	public String getSynonymSchemaName() {
		return synonymSchemaName;
	}

	public void setSynonymSchemaName(String synonymSchemaName) {
		this.synonymSchemaName = synonymSchemaName;
	}

	public Boolean getPublic() {
		if ( isPublic == null ) {
			isPublic = Boolean.FALSE;
		}
		return isPublic;
	}

	public void setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Boolean getForce() {
		return force;
	}

	public void setForce(Boolean force) {
		this.force = force;
	}

    @Override
    public String getConfirmationMessage() {
        return MessageFormat.format("Synonym {0} dropped", getSynonymName());
    }

	@Override
	public SqlStatement[] generateStatements(Database database) {
		DropSynonymStatement statement = new DropSynonymStatement();
		statement.setForce(getForce());
		statement.setSynonymName(getSynonymName());
		statement.setSynonymSchemaName(getSynonymSchemaName());
		statement.setPublic(getPublic());
		return new SqlStatement[] { statement };
	}

}
