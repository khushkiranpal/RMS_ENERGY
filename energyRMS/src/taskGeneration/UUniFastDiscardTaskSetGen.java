/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taskGeneration;

import java.util.Random;

/**
 *
 * @author KIRAN
 */
public class UUniFastDiscardTaskSetGen {
    private final long MAX_PERIOD;
	private final Random random = new Random();
	private final ITaskGenerator taskGenerator;
	private final int nbTask;
	private final double utilization;
	private final int deadlineModel;
       
	
	public UUniFastDiscardTaskSetGen(ITaskGenerator taskGen, int nbTask,
			 double utilization, int deadlineModel, long mAX_PERIOD) {

		if (utilization > 1) {
			throw new IllegalArgumentException(
					"Utilization must be less than or equal to 1.");
		}
		
		this.taskGenerator = taskGen;
		this.nbTask = nbTask;
		this.utilization = utilization;
		this.deadlineModel = deadlineModel;
		this.MAX_PERIOD = mAX_PERIOD;
		}
        public ITask[] generate() {
		ITask[] taskset = new ITask[nbTask];
                double[] util;

			util = generateUtilizations();
			for (int i = 0; i < util.length; i++) {
				taskset[i] = taskGenerator.generate(util[i], deadlineModel,MAX_PERIOD);
                                
			}
			// taskGenerator.finalizeTaskset(taskset, nbProc);
			return taskset;
        }
        private double[] generateUtilizations() {
		double[] util = new double
				[nbTask];
		double nextSumU;
		boolean discard;

		do {
			double sumU = utilization;
			discard = false;
			for (int i = 0; i < nbTask - 1; i++) {
				nextSumU = sumU	* Math.pow(random.nextDouble(), (double) 1/ (nbTask - (i + 1)));
				util[i] = sumU - nextSumU;
				sumU = nextSumU;
				if (util[i] > 1) {
					discard = true;
				}
			}
			util[nbTask - 1] = sumU;
			if (util[nbTask - 1] > 1) {
				discard = true;
			}
		} while (discard || !utilizationIsValid(util));
		return util;
	}
	private boolean utilizationIsValid(double[] util) {
		double sum = 0;

		for (double u : util) {
			sum += u;
		}
		return (sum <= 1) ? true : false;
	}
    
}
