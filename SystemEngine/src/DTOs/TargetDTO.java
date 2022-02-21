package DTOs;

import DTOs.TasksPrefernces.CompilationParameters;
import DTOs.TasksPrefernces.SimulationParameters;
import dependency.graph.DependencyGraph;
import dependency.target.Target;

public class TargetDTO {

    private String name;
    private Target.TaskResult result;
    private Target.TargetStatus status;
    private String runLog;
    private String extraData;
    private DependencyGraph.TaskType taskType;
    private String missionName;
    private String runBy;
    private SimulationParameters simulationParameters;
    private CompilationParameters compilationParameters;
    private int payment;

    public String getMissionName() {
        return missionName;
    }

    public TargetDTO(String name, String extraData, MissionInfoDTO missionInfoDTO) {
        this.name = name;
        this.extraData = extraData;
        this.runLog = "";
        this.taskType = missionInfoDTO.getMissionType();
        this.missionName = missionInfoDTO.getMissionName();
        this.compilationParameters = missionInfoDTO.getCompilationParameters();
        this.simulationParameters = missionInfoDTO.getSimulationParameters();
        this.payment = missionInfoDTO.getPriceByTaskType(missionInfoDTO.getMissionType());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Target.TaskResult getResult() {
        return result;
    }

    public void setResult(Target.TaskResult result) {
        this.result = result;
    }

    public Target.TargetStatus getTargetStatus() {
        return status;
    }

    public void setTargetStatus(Target.TargetStatus status) {
        this.status = status;
    }

    public String getRunLog() {
        return runLog;
    }

    public int getPayment() {
        return payment;
    }

    public void setRunLog(String runLog) {
        this.runLog = runLog;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public DependencyGraph.TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(DependencyGraph.TaskType taskType) {
        this.taskType = taskType;
    }

    public SimulationParameters getSimulationParameters() {
        return simulationParameters;
    }

    public CompilationParameters getCompilationParameters() {
        return compilationParameters;
    }

    public void setSimulationParameters(SimulationParameters simulationParameters) {
        this.simulationParameters = simulationParameters;
    }

    public void setCompilationParameters(CompilationParameters compilationParameters) {
        this.compilationParameters = compilationParameters;
    }

    public void setRunBy(String runBy) {
        this.runBy = runBy;
    }

    public String getRunBy() {
        return runBy;
    }

}
