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

package org.waarp.common.cpu;

import org.junit.Test;

import static org.junit.Assert.*;

public class CpuManagementTest {

  @Test
  public void testGetLoadAverage() {
    long total = 0;
    CpuManagement cpuManagement = null;
    try {
      cpuManagement = new CpuManagement();
    } catch (final UnsupportedOperationException e) {
      System.err.println(e);
      return;
    }
    double max = 0.0;
    System.err.println("LA: " + cpuManagement.getLoadAverage());
    for (int i = 0; i < 1000 * 1000 * 1000; i++) {
      // keep ourselves busy for a while ...
      // note: we had to add some "work" into the loop or Java 6
      // optimizes it away. Thanks to Daniel Einspanjer for
      // pointing that out.
      total += i;
      total *= 10;
    }
    if (total <= 0) {
      System.out.println(total);
    }
    System.err.println("LA: " + cpuManagement.getLoadAverage());
    total = 0;
    for (int i = 0; i < 1000 * 1000 * 1000; i++) {
      // keep ourselves busy for a while ...
      // note: we had to add some "work" into the loop or Java 6
      // optimizes it away. Thanks to Daniel Einspanjer for
      // pointing that out.
      total += i;
      total *= 10;
    }
    if (total <= 0) {
      System.out.println(total);
    }
    max = cpuManagement.getLoadAverage();
    System.err.println("LA: " + max);
    try {
      Thread.sleep(5000);
    } catch (final InterruptedException e) {
    }
    final double min = cpuManagement.getLoadAverage();
    System.err.println("LA: " + min);
    // Not checking since not as precise: assertTrue("Max > current: " + max + " >? " + min, max > min);

    total = 0;
    for (int i = 0; i < 1000 * 1000 * 1000 * 1000; i++) {
      // keep ourselves busy for a while ...
      // note: we had to add some "work" into the loop or Java 6
      // optimizes it away. Thanks to Daniel Einspanjer for
      // pointing that out.
      total += i;
      total *= 10;
    }
    if (total <= 0) {
      System.out.println(total);
    }
    max = cpuManagement.getLoadAverage();
    System.err.println("LA: " + max);
    // Not checking since not as precise: assertTrue("Min < current: " + min + " <? " + max, max >= min);
  }

  @Test
  public void testSysmonGetLoadAverage() {
    long total = 0;
    final CpuManagementSysmon cpuManagement = new CpuManagementSysmon();
    double max = 0.0;
    System.err.println("LAs: " + cpuManagement.getLoadAverage());
    for (int i = 0; i < 1000 * 1000 * 1000; i++) {
      // keep ourselves busy for a while ...
      // note: we had to add some "work" into the loop or Java 6
      // optimizes it away. Thanks to Daniel Einspanjer for
      // pointing that out.
      total += i;
      total *= 10;
    }
    if (total <= 0) {
      System.out.println(total);
    }
    System.err.println("LAs: " + cpuManagement.getLoadAverage());
    total = 0;
    for (int i = 0; i < 1000 * 1000 * 1000; i++) {
      // keep ourselves busy for a while ...
      // note: we had to add some "work" into the loop or Java 6
      // optimizes it away. Thanks to Daniel Einspanjer for
      // pointing that out.
      total += i;
      total *= 10;
    }
    if (total <= 0) {
      System.out.println(total);
    }
    max = cpuManagement.getLoadAverage();
    System.err.println("LAs: " + max);
    try {
      Thread.sleep(2000);
    } catch (final InterruptedException e) {
    }
    final double min = cpuManagement.getLoadAverage();
    System.err.println("LAs: " + min);
    assertTrue("Max > current: " + max + " >? " + min, max > min);

    total = 0;
    for (int i = 0; i < 1000 * 1000 * 1000; i++) {
      // keep ourselves busy for a while ...
      // note: we had to add some "work" into the loop or Java 6
      // optimizes it away. Thanks to Daniel Einspanjer for
      // pointing that out.
      total += i;
      total *= 10;
    }
    if (total <= 0) {
      System.out.println(total);
    }
    max = cpuManagement.getLoadAverage();
    System.err.println("LAs: " + max);
    assertTrue("Min < current: " + min + " <? " + max, max > min);
  }

}
