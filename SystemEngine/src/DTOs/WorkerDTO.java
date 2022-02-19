package DTOs;

public class WorkerDTO extends UserDTO{

    private Integer totalNumOfThreads;
    private Integer curNumOfWorkingThreads;

    public WorkerDTO(String Name, String Degree,Integer threadsAvailable){
        super(Name,Degree);
        this.totalNumOfThreads=threadsAvailable;
        curNumOfWorkingThreads=0;
    }

    public Integer getTotalNumOfThreads() {
        return totalNumOfThreads;
    }

    public Integer getCurNumOfWorkingThreads() {
        return curNumOfWorkingThreads;
    }
}
