/*
 * This file is part of Waarp Project (named also Waarp or GG).
 *
 *  Copyright (c) 2019, Waarp SAS, and individual contributors by the @author
 *  tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 *
 *  All Waarp Project is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Waarp is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 * Waarp . If not, see <http://www.gnu.org/licenses/>.
 */
package org.waarp.openr66.context.task;

import org.waarp.common.file.AbstractDir;
import org.waarp.common.logging.WaarpLogger;
import org.waarp.common.logging.WaarpLoggerFactory;
import org.waarp.openr66.context.R66Session;
import org.waarp.openr66.context.task.exception.OpenR66RunnerException;

import java.io.File;

/**
 * This task validate the File Path according to the following argument:<br>
 * - the full path is get from the current file<br>
 * - the arg path is transformed as usual (static and dynamic from information
 * transfer) and should be the
 * beginning of the correct valid path<br>
 * - the full path should begin with one of the result of the transformation
 * (blank separated)<br>
 * <br>
 * For instance "#OUTPATH# #INPATH# #WORKPATH# #ARHCPATH#" will test that the
 * current file is in one of the
 * standard path.
 *
 *
 */
public class ValidFilePathTask extends AbstractTask {
  /**
   * Internal Logger
   */
  private static final WaarpLogger logger =
      WaarpLoggerFactory.getLogger(ValidFilePathTask.class);

  /**
   * @param argRule
   * @param delay
   * @param argTransfer
   * @param session
   */
  public ValidFilePathTask(String argRule, int delay, String argTransfer,
                           R66Session session) {
    super(TaskType.VALIDFILEPATH, delay, argRule, argTransfer, session);
  }

  @Override
  public void run() {
    String finalname = argRule;
    finalname = AbstractDir
        .normalizePath(getReplacedValue(finalname, argTransfer.split(" ")));
    logger.info("Test Valid Path with " + finalname + " from {}", session);
    final File from = session.getFile().getTrueFile();
    final String curpath = AbstractDir.normalizePath(from.getAbsolutePath());
    final String[] paths = finalname.split(" ");
    for (final String base : paths) {
      if (curpath.startsWith(base)) {
        if (delay > 0) {
          logger.info(
              "Validate File " + curpath + " from " + base + " and     " +
              session.toString());
        }
        futureCompletion.setSuccess();
        return;
      }
    }
    if (delay > 0) {
      logger
          .error("Unvalidate File: " + curpath + "     " + session.toString());
    }
    futureCompletion
        .setFailure(new OpenR66RunnerException("File not Validated"));
  }

}
