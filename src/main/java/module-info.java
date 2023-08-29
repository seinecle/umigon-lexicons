module net.clementlevallois.umigon.heuristics {
    requires net.clementlevallois.umigon.model;
    requires net.clementlevallois.umigon.model.classification;
    requires net.clementlevallois.utils;
    requires net.clementlevallois.stopwords;
    requires mvel2;
    
    exports net.clementlevallois.umigon.heuristics.tools;
    exports net.clementlevallois.umigon.heuristics.booleanconditions;
}
