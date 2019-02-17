package com.example.micha.myapplication.Model;

import java.util.ArrayList;
import java.util.HashMap;
//Klasa potrzebna przy usuwaniu i dodawaniu parametrow za pomoca menu
public class ParamIdName  {
    private int id;
    private String name;

    ArrayList<NodeAdd> nodeAdds;


    public ParamIdName(int id, String name) {
        this.id = id;
        this.name = name;

        nodeAdds= new ArrayList<>();

    }


    @Override
    public String toString() {
        return "ParamIdName{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nodeAdds=" + nodeAdds +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public ArrayList<NodeAdd> getNodeAdds() {
        return nodeAdds;
    }



    public void addNodeAdd(int parentId, String name, String id,String idp){
        nodeAdds.add(new NodeAdd(parentId,name,id,idp));
    }




    public class NodeAdd {
        public int parentId;
        public HashMap<String, String> hashmap;

        public NodeAdd(int parentId, String name, String id,String idp) {
            this.parentId = parentId;
            this.hashmap = new HashMap<>();
            hashmap.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, name);
            hashmap.put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
            hashmap.put(ConstantManager.Parameter.ID, id);
            hashmap.put(ConstantManager.Parameter.IDP,idp);
        }
    }

}
