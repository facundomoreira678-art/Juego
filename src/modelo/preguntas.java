package modelo;



public class preguntas {
 private int id;
    private categorias categoria;
    private String pregunta;
    private String opcion_a;
    private String opcion_b;
    private String opcion_c;
    private String opcion_d;
    private String respuesta_correcta;
    private String dificultad;
   

    public preguntas(int id, categorias categoria, String pregunta, String opcion_a, String opcion_b, String opcion_c, String opcion_d, String respuesta_correcta, String dificultad) {
        this.id = id;
        this.categoria = categoria;
        this.pregunta = pregunta;
        this.opcion_a = opcion_a;
        this.opcion_b = opcion_b;
        this.opcion_c = opcion_c;
        this.opcion_d = opcion_d;
        this.respuesta_correcta = respuesta_correcta;
        this.dificultad = dificultad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public categorias getCategoria() { return categoria; }
    public void setCategoria(categorias categoria) { this.categoria = categoria; }
    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }
    public String getOpcion_a() { return opcion_a; }
    public void setOpcion_a(String opcion_a) { this.opcion_a = opcion_a; }
    public String getOpcion_b() { return opcion_b; }
    public void setOpcion_b(String opcion_b) { this.opcion_b = opcion_b; }
    public String getOpcion_c() { return opcion_c; }
    public void setOpcion_c(String opcion_c) { this.opcion_c = opcion_c; }
    public String getOpcion_d() { return opcion_d; }
    public void setOpcion_d(String opcion_d) { this.opcion_d = opcion_d; }
    public String getRespuesta_correcta() { return respuesta_correcta; }
    public void setRespuesta_correcta(String respuesta_correcta) { this.respuesta_correcta = respuesta_correcta; }
    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

}
