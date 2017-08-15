/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taskGeneration;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author KIRAN
 */
public  class Task implements ITask {
        private long id;
	private long arrival;
	private final long WCET_orginal;
	private long wcet;
	private double BCET;
	private double ACET;
	//private final long wcee;
	private final long period;
	private final long deadline;
	private int priority;
	private int nextJobId = 1;
	private static long count=0;
    private Job lastExecutedJob;
    private boolean preemptive = true;
    private float u =0;
  	private int type=-1;  // 1 for heavy weight or -1 light weight
    private double Slack=0;
    private double ResponseTime;
   private boolean is_Schedulabe=true;
	private long finishTime=1;   //finishTime for mpn-EDf eq 4
	// ENERGY PARAMETERS 
	private double energy_consumed ;
	private double frequency=1;
	private double voltage;
	

	
	public Task(long arrival, long wcet, long period,long deadline, int priority) {
		
		this.arrival = arrival;
		this.wcet = wcet;
		this.WCET_orginal = wcet;
		this.period = period;
		this.deadline = deadline;
		this.priority = priority;
		//this.id  = id;
		this.id = ++count;
	}
	
	public Task(long arrival,long id, long wcet, long period,long deadline, int priority) {
		
		this.arrival = arrival;
		this.wcet = wcet;
		this.WCET_orginal = wcet;
		this.period = period;
		this.deadline = deadline;
		this.priority = priority;
		this.id  = id;
		//this.id = ++count;
	}
	
public Task(long arrival,long id, long wcet, long period,long deadline, int priority, float u) {
		
		this.arrival = arrival;
		this.wcet = wcet;
		this.WCET_orginal = wcet;
		this.period = period;
		this.deadline = deadline;
		this.priority = priority;
		this.id  = id;
		this.u=u;
		//this.id = ++count;
	}

     



	/**
 * @return the bCET
 */
public double getBCET() {
	return BCET;
}

/**
 * @param bCET the bCET to set
 */
public void setBCET(double bCET) {
	BCET = bCET;
}

/**
 * @return the aCET
 */
public double getACET() {
	return ACET;
}

/**
 * @param aCET the aCET to set
 */
public void setACET(double aCET) {
	ACET = aCET;
}

	/**
 * @return the wCET_orginal
 */
public long getWCET_orginal() {
	return WCET_orginal;
}

	/**
	 * @param wcet the wcet to set public void setWcet(long wcet);
	 */
	public void setWcet(long wcet) {
		this.wcet = wcet;
	}
	
	public void setWcet(Double wcet) {
		this.wcet = wcet.longValue();
	}

	/**
 * @param wcet the wcet to set
 */


	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
        public long getArrival() {
		return arrival;
	}
	public void setArrival(long arrival){
		this.arrival = arrival;
	}
	
	public long getWcet() {
		return wcet;
	}

	public long getPeriod() {
		return period;
	}

	public long getDeadline() {
		return deadline;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void setPreemptive(boolean x)
	{
		preemptive= x;
	}
	
	public boolean getIsPreemptive()
	{
		return preemptive;
	}
       
        public ITask cloneTask() {
		return new Task(arrival,id, WCET_orginal,  period, deadline,  priority);
	}
    
        
	private final PriorityQueue<Job> activeJobs = new PriorityQueue<Job>(2,
			new Comparator<Job>() {
				public int compare(Job j1, Job j2) {
					return (int) (j1.getActivationDate() - j2.getActivationDate());
				}
			});
	
	public void addactivatedjob(Job j)
	{
		activeJobs.add(j);
	}


	//@Override
	public PriorityQueue<Job> getActiveJobs() {
		return activeJobs;
	}

	public boolean isActive() {
		return !getActiveJobs().isEmpty();
	}
        
       

	public  Job activate(long time) {
                JobId jobId = new JobId(this.getId(),nextJobId++);
         //       System.out.println("in task     "+"job id "+jobId.getJobId()+"  task id  " + jobId.getTaskId());
		 Job job = new  Job(jobId, time, WCET_orginal,wcet, time + deadline,preemptive, type);
		//getActiveJobs().add(job);
                //return job;
		return job;
	}

	public  Job activateRMS(long time) {
        JobId jobId = new JobId(this.getId(),nextJobId++);
        //       System.out.println("in task     "+"job id "+jobId.getJobId()+"  task id  " + jobId.getTaskId());
        	Job job = new  Job(jobId, time, WCET_orginal, wcet, time + deadline, period);
        		//getActiveJobs().add(job);
        		//return job;
        		return job;
		}
	public  Job activateRMS_energy(long time) {
        JobId jobId = new JobId(this.getId(),nextJobId++);
        //       System.out.println("in task     "+"job id "+jobId.getJobId()+"  task id  " + jobId.getTaskId());
        	Job job = new  Job(jobId, time, WCET_orginal, wcet, time + deadline, period, frequency, (long)(Slack+ time));
        		//getActiveJobs().add(job);
        		//return job;
        		return job;
		}
	
	public  Job getCurrentJob() {
		return getActiveJobs().peek();
	}

	public boolean checkDeadlineMissed(long time) {
		 Job job = getCurrentJob();
		if (job == null)
			return false;
		if (job.getRomainingTimeCost() > 0 && time >= job.getAbsoluteDeadline())
			return true;

		return false;
	}

	public boolean lastExecutedJobHasCompleted() {
		return getLastExecutedJob() != getActiveJobs().peek();

	}

	public long getRemainingCost() {
		if (getCurrentJob() != null)
			return getCurrentJob().getRomainingTimeCost();
		return 0;
	}

	public long getNextDeadline(long time, boolean nextPeriod) {
		if (time <  arrival)
			return  arrival + deadline;

		long currentPeriod = (time -  arrival) / period;

//		if (nextPeriod && getCurrentJob() != null
//				&& getCurrentJob().getRomainingTimeCost() == 0)
		if(nextPeriod && getRemainingCost()==0)
			currentPeriod++;

		return  arrival + currentPeriod * period + deadline;
	}

	public long getNextActivation(long time) {
		if (time <  arrival)
			return  arrival;
		return  arrival + (time / period) * period + period;
	}

	public boolean isActivationTime(long time) {
		if (time <  arrival)
			return false;
		return (time -  arrival) % period == 0;
	}

	public long getNextAbsoluteDeadline(long time) {
		long currentPeriod = (time -  arrival) / period;
		return  arrival + currentPeriod * period + deadline;
	}

	public long getPreviousAbsoluteDeadline(long time) {
		long currentPeriod = time / period;
		long t;
		do {
			if (currentPeriod < 0)
				return -1;
			t = arrival + currentPeriod * period + deadline;
			currentPeriod--;
		} while (t >= time);

		return t;
	}

	public  Job getLastExecutedJob() {
		return lastExecutedJob;

	}

	public void setLastExecutedJob( Job lastExecutedJob) {
		this.lastExecutedJob = lastExecutedJob;

	}
	 /**
		 * @return the type
		 */
		public int getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(int type) {
			this.type = type;
		}

		  /**
		 * @return the u
		 */
		public float getU() {
			return u;
		}
		/**
		 * @param u the u to set
		 */
		public void setU(float u) {
			this.u = u;
		}
		/**
		 * @return the slack
		 */
		public double getSlack() {
			return Slack;
		}
		/**
		 * @param slack the slack to set
		 */
		public void setSlack(double slack) {
			Slack = slack;
		}

		/**
		 * @return the responseTime
		 */
		public double getResponseTime() {
			return ResponseTime;
		}

		/**
		 * @param responseTime the responseTime to set
		 */
		public void setResponseTime(double responseTime) {
			ResponseTime = responseTime;
		}

		/**
		 * @return the is_Schedulabe
		 */
		public boolean isIs_Schedulabe() {
			return is_Schedulabe;
		}

		/**
		 * @param is_Schedulabe the is_Schedulabe to set
		 */
		public void setIs_Schedulabe(boolean is_Schedulabe) {
			this.is_Schedulabe = is_Schedulabe;
		}
		/**
		 * @return the finishTime for mpn-EDf eq 4
		 */
		public long getFinishTime() {
			return finishTime;
		}
       
		
		
		
		/**
		 * @return the energy_consumed
		 */
		public double getEnergy_consumed() {
			return energy_consumed;
		}

		/**
		 * @param energy_consumed the energy_consumed to set
		 */
		public void setEnergy_consumed(double energy_consumed) {
			this.energy_consumed = energy_consumed;
		}

		/**
		 * @return the frequency
		 */
		public double getFrequency() {
			return frequency;
		}

		/**
		 * @param frequency the frequency to set
		 */
		public void setFrequency(double frequency) {
			this.frequency = frequency;
		}

		/**
		 * @return the voltage
		 */
		public double getVoltage() {
			return voltage;
		}

		/**
		 * @param voltage the voltage to set
		 */
		public void setVoltage(double voltage) {
			this.voltage = voltage;
		}

		/**
		 * @param finishTime the finishTime to set
		 */
		public void setFinishTime(long finishTime) {
			this.finishTime = finishTime;
		}

		
}
