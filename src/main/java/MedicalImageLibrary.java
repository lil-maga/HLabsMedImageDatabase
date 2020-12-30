import java.io.Serializable;
import java.util.ArrayList;
//Class that stores an arraylist of medical images
public class MedicalImageLibrary implements Serializable {
    //Declares an array list "Library" of medical images
    protected ArrayList<MedImage> Library = new ArrayList<>();

    //Getter and setter for image library
    public ArrayList<MedImage> getLibrary() {
        return Library;
    }
    public void setLibrary(ArrayList<MedImage> library) {
        Library = library;
    }

    //Method to add an image to the medical image library
    public void AddNewImage(MedImage img){
        Library.add(img);
    }

}