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

package org.waarp.ftp.client;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.waarp.common.logging.WaarpLogger;
import org.waarp.common.logging.WaarpLoggerFactory;
import org.waarp.ftp.client.transaction.FtpApacheClientTransactionTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractFtpClientWithApache extends AbstractFtpClient {
  /**
   * Internal Logger
   */
  protected static WaarpLogger logger =
      WaarpLoggerFactory.getLogger(AbstractFtpClientWithApache.class);
  private static final int port = 2021;

  @Test
  public void test0_FtpApacheClientActive() throws IOException {
    final File localFilename = new File("/tmp/ftpfile.bin");
    final FileWriter fileWriterBig = new FileWriter(localFilename);
    for (int i = 0; i < 100; i++) {
      fileWriterBig.write("0123456789");
    }
    fileWriterBig.flush();
    fileWriterBig.close();
    logger.warn("Active");
    launchFtpClient("127.0.0.1", port, "fredo", "fred1", "a", true,
                    localFilename.getAbsolutePath(), localFilename.getName());
    logger.warn("End Active");
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      // ignore
    }
  }

  @Test
  public void test0_FtpApacheClientPassive() throws IOException {
    final File localFilename = new File("/tmp/ftpfile.bin");
    final FileWriter fileWriterBig = new FileWriter(localFilename);
    for (int i = 0; i < 100; i++) {
      fileWriterBig.write("0123456789");
    }
    fileWriterBig.flush();
    fileWriterBig.close();
    logger.warn("Passive");
    launchFtpClient("127.0.0.1", port, "fredo", "fred1", "a", false,
                    localFilename.getAbsolutePath(), localFilename.getName());
    logger.warn("End Passive");
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      // ignore
    }
  }

  private void internalApacheClient(FtpApacheClientTransactionTest client,
                                    File localFilename, int delay,
                                    boolean mode) {
    final String smode = mode? "passive" : "active";
    logger.info(" transfer {} store ", smode);
    if (!client
        .transferFile(localFilename.getAbsolutePath(), localFilename.getName(),
                      true)) {
      logger.warn("Cant store file {} mode ", smode);
      FtpClientTest.numberKO.incrementAndGet();
      return;
    } else {
      FtpClientTest.numberOK.incrementAndGet();
      if (delay > 0) {
        try {
          Thread.sleep(delay);
        } catch (final InterruptedException ignored) {
        }
      }
    }
    if (!client.deleteFile(localFilename.getName())) {
      logger.warn(" Cant delete file {} mode ", smode);
      FtpClientTest.numberKO.incrementAndGet();
      return;
    } else {
      FtpClientTest.numberOK.incrementAndGet();
      if (delay > 0) {
        try {
          Thread.sleep(delay);
        } catch (final InterruptedException ignored) {
        }
      }
    }
    if (!client
        .transferFile(localFilename.getAbsolutePath(), localFilename.getName(),
                      true)) {
      logger.warn("Cant store file {} mode ", smode);
      FtpClientTest.numberKO.incrementAndGet();
      return;
    } else {
      FtpClientTest.numberOK.incrementAndGet();
      if (delay > 0) {
        try {
          Thread.sleep(delay);
        } catch (final InterruptedException ignored) {
        }
      }
    }
    Thread.yield();
    logger.info(" transfer {} retr ", smode);
    if (!client.transferFile(null, localFilename.getName(), false)) {
      logger.warn("Cant retrieve file {} mode ", smode);
      FtpClientTest.numberKO.incrementAndGet();
    } else {
      FtpClientTest.numberOK.incrementAndGet();
      if (delay > 0) {
        try {
          Thread.sleep(delay);
        } catch (final InterruptedException ignored) {
        }
      }
    }
  }

  @Test
  public void test2_FtpSimple() throws IOException {
    numberKO.set(0);
    numberOK.set(0);
    final File localFilename = new File("/tmp/ftpfile.bin");

    final int delay = 50;

    final FtpApacheClientTransactionTest client =
        new FtpApacheClientTransactionTest("127.0.0.1", port, "fred", "fred2",
                                           "a", SSL_MODE);
    if (!client.connect()) {
      logger.error("Can't connect");
      FtpClientTest.numberKO.incrementAndGet();
      assertTrue("No KO", numberKO.get() == 0);
      return;
    }
    try {
      logger.warn("Create Dirs");
      client.makeDir("T" + 0);
      logger.warn("Feature commands");
      System.err.println("SITE: " + client.featureEnabled("SITE"));
      System.err.println("SITE CRC: " + client.featureEnabled("SITE XCRC"));
      System.err.println("CRC: " + client.featureEnabled("XCRC"));
      System.err.println("MD5: " + client.featureEnabled("XMD5"));
      System.err.println("SHA1: " + client.featureEnabled("XSHA1"));
      System.err.println("DIGEST: " + client.featureEnabled("XDIGEST"));
      client.changeDir("T0");
      try {
        Thread.sleep(delay);
      } catch (final InterruptedException ignored) {
      }
      client.changeFileType(true);
      client.changeMode(true);
      internalApacheClient(client, localFilename, delay, true);
      client.changeMode(false);
      internalApacheClient(client, localFilename, delay, false);
    } finally {
      logger.warn("Logout");
      client.logout();
      client.disconnect();
    }
    assertEquals("No KO", 0, numberKO.get());
  }
}