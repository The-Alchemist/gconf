/**
 * Copyright 2015 Yves Galante
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jmob.guice.conf.core.samples.complex;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import net.jmob.guice.conf.core.ConfigurationModule;
import net.jmob.guice.conf.core.samples.complex.service.Service;
import net.jmob.guice.conf.core.samples.complex.service.ServiceConf;
import net.jmob.guice.conf.core.samples.complex.service.ServiceJson;
import net.jmob.guice.conf.core.samples.complex.service.TypedEntry;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Named;

import static com.google.inject.name.Names.named;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class Sample03 {

    @Inject
    @Named("Conf")
    private Service serviceConf;

    @Inject
    @Named("Json")
    private Service serviceJson;

    @Before
    public void init() {
        Guice.createInjector(new SampleModule(this));
    }

    @Test
    public void testConf() {
        assertThat(serviceConf.getPort(), is(12));
        check(serviceConf);
    }

    @Test
    public void testJson() {
        assertThat(serviceJson.getPort(), is(13));
        check(serviceJson);
    }

    private void check(Service service) {
        final TypedEntry config = service.getConfig();
        assertThat(config, notNullValue());

        assertThat(config.getValue(), is("Hello World"));
        assertNotNull(config.getAMap());
        assertThat(config.getAMap(), notNullValue());
        assertThat(config.getAMap().get("key1"), is("value1"));
        assertThat(config.getAMap().get("key2"), is("value2"));
        assertThat(config.getAList().get(0), is("value1"));
        assertThat(config.getAList().get(1), is("value2"));

        assertThat(config.toString(), is("{getAList=[value1, value2], getValue=Hello World, " +
                "getTypedMap={entry1={getValue=Hello 1, getIntValue=1234}, entry2={getValue=Hello 2}}, " +
                "getAMap={key1=value1, key2=value2}}"));
        assertThat(config.hashCode(), is(-1633332542));
    }

    public static class SampleModule extends AbstractModule {

        private final Sample03 sample03;

        public SampleModule(Sample03 sample03) {
            this.sample03 = sample03;
        }

        @Override
        protected void configure() {
            install(ConfigurationModule.create());
            requestInjection(sample03);
            bind(Service.class).annotatedWith(named("Conf")).to(ServiceConf.class);
            bind(Service.class).annotatedWith(named("Json")).to(ServiceJson.class);
        }
    }
}
