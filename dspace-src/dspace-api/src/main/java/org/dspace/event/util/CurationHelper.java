package org.dspace.event.util;

import org.dspace.event.consumer.QueueTaskOnEvent;
import org.apache.log4j.Logger;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.curate.Curator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Helper class to deal with curation tasks.
 * @author Andrea Schweer schweer@waikato.ac.nz for LCoNZ IRR project
 *
 * @author Pratyusha Doddapaneni pratyusha.doddapaneni@ed.ac.uk 
 * Customised for REDIC project
 */
public class CurationHelper {
	private static Logger log = Logger.getLogger(CurationHelper.class);

	private List<String> taskNames = new ArrayList<String>();
	private String queueName = "continually";
	private ArrayList<Item> toQueue;

	public void initQueueName(String queueProperty) {
		log.info(" initQueueName - Curation Helper\n");
		String queueConfig = ConfigurationManager.getProperty("install_curate", queueProperty);
		if (queueConfig != null && !"".equals(queueConfig)) {
			queueName = queueConfig;
			log.info("Using queue name " + queueName);
		} else {
			log.info("No queue name specified, using default: " + queueName);
		}
	}

	public void initTaskNames(String tasksProperty) {
		log.info(" initTaskName - Curation Helper\n");
		List<String> taskNamesList;
		String taskConfig = ConfigurationManager.getProperty("install_curate", tasksProperty);
		if (taskConfig == null || "".equals(taskConfig)) {
			taskNamesList = Collections.emptyList();
		} else {
			taskNamesList = Arrays.asList(taskConfig.split("\\s*,\\s*"));
		}
		taskNames.addAll(taskNamesList);
		log.info("Setting up tasks as " + Arrays.deepToString(taskNames.toArray()));
	}

	public void addToQueue(Item item) {
		log.info(" addToQueue - Curation Helper\n");
		if (toQueue == null) {
			toQueue = new ArrayList<Item>();
		}
		toQueue.add(item);
		log.info("Adding item " + item.getHandle() + " to list of items to queue");
	}

	public void queueForCuration(Context ctx) throws IOException {
		log.info(" queueforCuration - Curation Helper\n");
		if (toQueue != null && !toQueue.isEmpty()) {
			log.info("Actually queueing " + toQueue.size() + " items for curation");
			for (String taskName : taskNames) {
				Curator curator = new Curator().addTask(taskName);
				for (Item item : toQueue) {
					String identifier;
					if (item.getHandle() != null) {
						identifier = item.getHandle();
					} else {
						identifier = item.getID()  + "";
					}
					log.info("Queued item " + identifier + " for curation in queue " + queueName + ", task " + taskName);
					curator.queue(ctx, identifier, queueName);
				}
			}
		}
		toQueue = null;
	}

	public boolean hasTaskNames() {
		log.info(" hastasknames - Curation Helper\n");
		return !taskNames.isEmpty();
	}
}
