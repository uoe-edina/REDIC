package org.dspace.event.consumer;

import org.dspace.event.util.CurationHelper;
import org.apache.log4j.Logger;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.event.Consumer;
import org.dspace.event.Event;

import java.sql.SQLException;

/**
 *  @author Andrea Schweer schweer@waikato.ac.nz for LCoNZ IRR project
 *
 * Abstract event consumer that queues curation tasks when specific events occur.
 */
public abstract class QueueTaskOnEvent implements Consumer {
	private static Logger log = Logger.getLogger(QueueTaskOnEvent.class);

	private CurationHelper helper;

	public void initialize() throws Exception {
		helper = new CurationHelper();
		helper.initTaskNames(getTasksProperty());
		if (!helper.hasTaskNames()) {
			log.error("QueueTaskOnEvent: no configuration value found for tasks to queue (" + getTasksProperty() + "), can't initialise.");
			return;
		}
		log.info("Initialoze - QueueTaskOnEvent\n");

		helper.initQueueName(getQueueProperty());
	}

	public void consume(Context ctx, Event event) throws Exception {
		Item item = null;

		if (isApplicableEvent(ctx, event)) {
			item = findItem(ctx, event);
		} else {
			log.info("Event is not applicable, skipping");
		}

		if (item == null) {
			// not applicable -> skip
			log.info("Can't find item to work on, skipping");
			return;
		}
		log.info("Consume - QueueTaskOnEvent\n");

		helper.addToQueue(item);
	}

	abstract Item findItem(Context ctx, Event event) throws SQLException;

	abstract boolean isApplicableEvent(Context ctx, Event event) throws SQLException;

	public void end(Context ctx) throws Exception {
		helper.queueForCuration(ctx);
	}

	public void finish(Context ctx) throws Exception {
	}

	abstract String getTasksProperty();

	abstract String getQueueProperty();
}
