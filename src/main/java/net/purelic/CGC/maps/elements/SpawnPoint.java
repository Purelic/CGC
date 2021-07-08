package net.purelic.CGC.maps.elements;

import net.purelic.CGC.maps.NestedMapElement;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.commons.utils.YamlUtils;

import java.util.Map;

@SuppressWarnings("unchecked")
public class SpawnPoint<E extends Enum<E>> extends NestedMapElement<E> {

    public SpawnPoint(Map<String, Object> yaml) {
        super(MapElementType.SPAWN, (Class<E>) MatchTeam.class, "team", false, yaml);
    }

    @Override
    public String getBookHover() {
        return YamlUtils.formatCoords(this.getLocation(), true, false);
    }

}
