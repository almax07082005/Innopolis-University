package Week9;

public class Document implements DocumentPrototype {
    private DocumentState documentState;

    public Document(DocumentState documentState) {
        this.documentState = documentState;
    }

    public void changeState(DocumentState newState) {
        documentState = newState;
    }

    @Override
    public DocumentPrototype _clone() {
        return new Document(documentState);
    }
}
