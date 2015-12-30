import java.util.Comparator;

public class RowComparator implements Comparator<Data>
{
    @Override
    public int compare(Data x, Data y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
    	//TODO: replace 0 with column number
        if (Integer.parseInt(x.rows.get(0).split(",")[0]) > Integer.parseInt(y.rows.get(0).split(",")[0]))
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
