package Entities;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicalImageLibrary implements Serializable {
    protected ArrayList<MedImage> Library = new ArrayList<>();

    public ArrayList<MedImage> getLibrary() {
        return Library;
    }

    public void setLibrary(ArrayList<MedImage> library) {
        Library = library;
    }

    public void AddNewImage(MedImage img){
        Library.add(img);
    }

    public Integer getSize(){
        return Library.size();
    }

    public boolean isEmpty(){
        return Library.isEmpty();
    }

    public MedImage get(int i){
        return Library.get(i);
    }

}