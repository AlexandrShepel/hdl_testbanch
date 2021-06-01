package alex.shepel.hdl_testbench.backend.parser.detectors;

/*
 * File: PortDescriptor.java
 * -----------------------------------------------
 * Stores a full port's description
 * of a SystemVerilog/Verilog module.
 */
public class PortDescriptor {

    /* Port's properties. */
    private String name = "";
    private String type = "";
    private String signed = "";
    private String packedSize = "";
    private String unpackedSize = "";

    /* Getters and setters of the port's properties. */
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setSigned(String signed) {
        this.signed = signed;
    }
    public String getSigned() {
        return signed;
    }

    public void setPackedSize(String packedSize) {
        this.packedSize = packedSize;
    }
    public String getPackedSize() {
        return packedSize;
    }

    public String getUnpackedSize() {
        return unpackedSize;
    }
    public void setUnpackedSize(String unpackedSize) {
        this.unpackedSize = unpackedSize;
    }

    /**
     * Collects port's properties to a one string.
     * Result have a form of SystemVerilog/Verilog
     * module's port declaration.
     *
     * @return The Sting value that contains
     *         SystemVerilog/Verilog module's port declaration.
     */
    public String toString() {
        String toString = "bit";

        if (!signed.equals(""))
            toString = toString + " " + signed;

        if (!packedSize.equals(""))
            toString = toString + " " + packedSize;

        toString = toString + " " + name;

        if (!unpackedSize.equals(""))
            toString = toString + " " + unpackedSize;

        return toString;
    }

    /**
     * Collects port's properties to a new PortDescriptor object
     * with preset properties.
     * Allows to copy a port properties to another PortDescriptor object.
     *
     * @param portDescriptor The new PortDescriptor object
     *                       with preset properties.
     */
    public void copyDescription(PortDescriptor portDescriptor) {
        name = portDescriptor.getName();
        type = portDescriptor.getType();
        signed = portDescriptor.getSigned();
        packedSize = portDescriptor.getPackedSize();
        unpackedSize = portDescriptor.getUnpackedSize();
    }

}
