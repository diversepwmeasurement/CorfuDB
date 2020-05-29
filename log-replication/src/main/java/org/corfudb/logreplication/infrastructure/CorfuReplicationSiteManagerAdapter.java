package org.corfudb.logreplication.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.corfudb.logreplication.proto.LogReplicationSiteInfo.SiteConfigurationMsg;

import static org.corfudb.logreplication.infrastructure.DiscoveryServiceEvent.DiscoveryServiceEventType.DiscoverySite;

public abstract class CorfuReplicationSiteManagerAdapter {
    @Getter
    @Setter
    CorfuReplicationDiscoveryService corfuReplicationDiscoveryService;

    @Getter
    SiteConfigurationMsg siteConfigMsg;

    @Getter
    CrossSiteConfiguration siteConfig;

    public synchronized CrossSiteConfiguration fetchSiteConfig() {
        siteConfigMsg = querySiteConfig();
        siteConfig = new CrossSiteConfiguration(siteConfigMsg);
        return siteConfig;
    }

    /**
     * Will be called when the site change and a new configuration is sent over
     * @param newSiteConfigMsg
     * @return
     */
    synchronized void updateSiteConfig(SiteConfigurationMsg newSiteConfigMsg) {
            if (newSiteConfigMsg.getSiteConfigID() > siteConfigMsg.getSiteConfigID()) {
                siteConfigMsg = newSiteConfigMsg;
                siteConfig = new CrossSiteConfiguration(siteConfigMsg);
                corfuReplicationDiscoveryService.putEvent(
                        new DiscoveryServiceEvent(DiscoverySite, newSiteConfigMsg));
            }
    }

    public abstract SiteConfigurationMsg querySiteConfig();

    public abstract void start();
}