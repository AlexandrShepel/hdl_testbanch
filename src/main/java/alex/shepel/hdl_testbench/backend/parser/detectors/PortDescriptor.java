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
        StringBuilder result = new StringBuilder("bit");

        if (!signed.equals(""))
            result.append(" ").append(signed);

        if (!packedSize.equals(""))
            result.append(" ").append(packedSize);

        result.append(" ").append(name);

        if (!unpackedSize.equals(""))
            result.append(" ").append(unpackedSize);

        return result.toString();
    }

    public PortDescriptor deepCopy() {
        PortDescriptor portDescriptor = new PortDescriptor();

        portDescriptor.setName(name);
        portDescriptor.setType(type);
        portDescriptor.setSigned(signed);
        portDescriptor.setPackedSize(packedSize);
        portDescriptor.setUnpackedSize(unpackedSize);

        return portDescriptor;
    }

}
