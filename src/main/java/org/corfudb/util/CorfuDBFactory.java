package org.corfudb.util;

import org.corfudb.runtime.CorfuDBRuntime;
import org.corfudb.runtime.view.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by mwei on 5/1/15.
 */
public class CorfuDBFactory {

    Map<String, Object> options;

    public CorfuDBFactory(Map<String, Object> options)
    {
        this.options = options;
    }

    public CorfuDBRuntime getRuntime()
    {
        return new CorfuDBRuntime((String) options.get("--master"));
    }

    @SuppressWarnings("unchecked")
    public IWriteOnceAddressSpace getWriteOnceAddressSpace(CorfuDBRuntime cdr)
    {
        try {
            String type = (String) options.get("--address-space");
            Class<? extends IWriteOnceAddressSpace> addressSpaceClass = (Class<? extends IWriteOnceAddressSpace>) Class.forName("org.corfudb.runtime.view." + type);
            if (!Arrays.asList(addressSpaceClass.getInterfaces()).contains(IWriteOnceAddressSpace.class))
            {
                throw new Exception("Not a write once address space type");
            }
            return addressSpaceClass.getConstructor(CorfuDBRuntime.class).newInstance(cdr);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public IStreamingSequencer getStreamingSequencer(CorfuDBRuntime cdr)
    {
        return new StreamingSequencer(cdr);
    }

    public IConfigurationMaster getConfigurationMaster(CorfuDBRuntime cdr)
    {
        return new ConfigurationMaster(cdr);
    }
}