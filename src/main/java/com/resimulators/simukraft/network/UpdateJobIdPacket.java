package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateJobIdPacket implements IMessage {

    public UpdateJobIdPacket(){}

    public UpdateJobIdPacket(int id, int x, int y, int z)
    {
      this.id = id;
      this.x = x;
      this.y = y;
      this.z = z;
    }
    int id;
    int x;
    int y;
    int z;
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        System.out.println("Reading info");
        id = byteBuf.readInt();
        x = byteBuf.readInt();
        y = byteBuf.readInt();
        z = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(id);
        byteBuf.writeInt(x);
        byteBuf.writeInt(y);
        byteBuf.writeInt(z);

    }
}
