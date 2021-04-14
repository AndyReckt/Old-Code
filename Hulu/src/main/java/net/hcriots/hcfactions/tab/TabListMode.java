package net.hcriots.hcfactions.tab;

import lombok.AllArgsConstructor;

/**
 * @author Brandon Hall
 * Copyright @ Brandon Hall | All rights reserved
 */
@AllArgsConstructor
public enum TabListMode {

    DETAILED("Normal"),
    DETAILED_WITH_FACTION_INFO("Normal w/ Team List");

    private final String name;

    public String getName() {
        return name;
    }

}