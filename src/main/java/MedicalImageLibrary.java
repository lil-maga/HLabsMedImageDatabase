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

}