/*
 * Copyright (C) 2017-2018 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.dac.server;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dremio.dac.model.sources.SourceUI;
import com.dremio.dac.model.spaces.Space;
import com.dremio.dac.service.errors.ClientErrorException;
import com.dremio.file.FileName;

/**
 * Test input validation
 */
public class TestInputValidation {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSpaceValidation() {
    checkError(new Space(null, "\"", null, null, null, 0, null));
    checkError(new Space(null, ".", null, null, null, 0, null));
    checkError(new Space(null, "longer \" test", null, null, null, 0, null));
  }

  @Test
  public void testSourceValidation() {
    SourceUI config = new SourceUI();
    config.setName("\"");
    checkError(config);

    config.setName(".");
    checkError(config);

    config.setName("longer \" test");
    checkError(config);
  }

  @Test
  public void testGeneralValidation() {
    checkError(new FileName("afaf:dadad"));
    checkError(new FileName(":"));
    checkError(new FileName("/"));
    checkError(new FileName("ada/adadad"));
    checkError(new FileName("adad@adad"));
    checkError(new FileName("adad{adad"));
    checkError(new FileName("."));
    checkError(new FileName(".adadd"));
    checkError(new FileName("ad.add"));
  }

  private void checkError(Object o) {
    checkError(o, null);
  }
  private void checkError(Object o, String expectedMessage) {
    thrown.expect(ClientErrorException.class);
    if(expectedMessage != null) {
      thrown.expectMessage(expectedMessage);
    }
    new InputValidation().validate(o);
  }
}

