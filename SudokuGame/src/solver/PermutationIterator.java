
package solver;


public class PermutationIterator {
    private int[] current = {1,1,1,1,1};
    private boolean hasMore = true;
    
    public boolean hasNext(){
        return hasMore;
    }
    
    public int[] next(){
        int[] result = current.clone();
        increment();
        return result;
    }

    private void increment() {
        
        int i = 4;
        while(i>=0){
            current[i]++;
            if(current[i] <= 9)
                return;
            
            current[i] = 1;
            i--;
        }
        
        hasMore = false;
    }
    
    
}
