package dataS;

/**
 * Data structure used to store window query result
 * @author Xuefei Li
 *
 */
public class QueryResultDataStruc 
{
	private int trajectoryID;
	private SamplePoint data;

	
	public QueryResultDataStruc(int trajectoryID, SamplePoint data)
	{
		super();
		this.trajectoryID = trajectoryID;
		this.data =data;
	}	
	
	public SamplePoint getData() {
		return data;
	}

	public void setData(SamplePoint data) {
		this.data = data;
	}

	public void setTrajectoryID(int trajectoryID)
	{
		this.trajectoryID = trajectoryID;
	}
	
	public int getTrajectoryID()
	{
		return trajectoryID;
	}
	
	
}

