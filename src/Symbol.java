enum Symbol {
    X('X'), O('O'), EMPTY('-');
    
    private final char value;
    
    Symbol(char value) {
        this.value = value;
    }
    
    public char getValue() {
        return value;
    }
}