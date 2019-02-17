package com.example.micha.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;
//Składowa SensorWithData
public class NodeSubgroup implements Parcelable {
    private int id;
    private int positionId;
    private int nodeId;
    private int parameterId;
    private int localId;
    private String description;

    public NodeSubgroup(int id, int positionId, int nodeId, int parameterId, int localId, String description) {
        this.id = id;
        this.positionId = positionId;
        this.nodeId = nodeId;
        this.parameterId = parameterId;
        this.localId = localId;
        this.description = description;
    }

    @Override
    public String toString() {
        return "NodeSubgroup{" +
                "id=" + id +
                ", positionId=" + positionId +
                ", nodeId=" + nodeId +
                ", parameterId=" + parameterId +
                ", localId=" + localId +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getPositionId() {
        return positionId;
    }

    public int getNodeId() {
        return nodeId;
    }
    public int getLocalId() {
        return localId;
    }

    public int getParameterId() {
        return parameterId;
    }

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(positionId);
        out.writeInt(nodeId);
        out.writeInt(parameterId);
        out.writeInt(localId);
        out.writeString(description);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<NodeSubgroup> CREATOR = new Parcelable.Creator<NodeSubgroup>() {
        public NodeSubgroup createFromParcel(Parcel in) {
            return new NodeSubgroup(in);
        }

        public NodeSubgroup[] newArray(int size) {
            return new NodeSubgroup[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private NodeSubgroup(Parcel in) {
        id = in.readInt();
        positionId=in.readInt();
        nodeId=in.readInt();
        parameterId=in.readInt();
        localId=in.readInt();
        description=in.readString();

    }
}
