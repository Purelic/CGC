package net.purelic.CGC.maps.constants;

public enum DependencyType {

    HILL(MapElementType.HILL),
    FLAG(MapElementType.FLAG),
    ;

    private final MapElementType elementType;

    DependencyType(MapElementType elementType) {
        this.elementType = elementType;
    }

    public MapElementType getElementType() {
        return this.elementType;
    }

}
