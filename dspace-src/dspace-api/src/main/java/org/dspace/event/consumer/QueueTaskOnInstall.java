package org.dspace.event.consumer;

import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.event.Event;
import org.apache.log4j.Logger;
import java.sql.SQLException;

/**
 *  @author Andrea Schweer schweer@waikato.ac.nz for LCoNZ IRR project
 *
 * Event consumer that queues curation tasks when an item is installed into the archive.
 *
 * Event consumers cannot make changes to their subject. Consequently, whenever a change to the item is desired on
 * installation, this needs to be performed by a curation task.
 *
 * This consumer queues up the curation tasks given in the configuration file via queue.install.tasks
 * (comma separated list) into the task queue named in the configuration file via queue.install.name
 * (default: continually). This task queue should then be run regularly and quite often, eg using a cronjob that runs
 * every minute or two.
 *
 */
public class QueueTaskOnInstall extends QueueTaskOnEvent {


	private static Logger log = Logger.getLogger(QueueTaskOnInstall.class);

	@Override
	String getTasksProperty() {
		log.info("getTasksProperty - QueueTaskOnInstall\n");
		return "queue.install.tasks";
	}

	@Override
	String getQueueProperty() {
		log.info("getQueueProperty - QueueTaskOnInstall\n");
		System.out.println("queue.install.name");
		return "queue.install.name";
	}

	@Override
	boolean isApplicableEvent(Context ctx, Event event) {
		return event.getSubjectType() == Constants.ITEM && event.getEventType() == Event.INSTALL;
	}

	Item findItem(Context ctx, Event event) throws SQLException {
		return (Item) event.getSubject(ctx);
	}
}
