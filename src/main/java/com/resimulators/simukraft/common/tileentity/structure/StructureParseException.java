package com.resimulators.simukraft.common.tileentity.structure;

/**
 * Created by Astavie on 26/01/2018 - 8:21 PM.
 */
public class StructureParseException extends RuntimeException {

	public StructureParseException(String msg) {
		super(msg);
	}

	public StructureParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public StructureParseException(Throwable cause) {
		super(cause);
	}

}
