package com.gmail.andersoninfonet.utils;

import java.io.PrintWriter;
import java.util.List;

import com.gmail.andersoninfonet.model.Content;

public class HTMLMarvelGenerator implements HTMLGenerator {
    
    private final PrintWriter printWriter;

    public HTMLMarvelGenerator(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    @Override
    public void generate(List<? extends Content> contents) {
        StringBuilder builder = new StringBuilder();
        builder
            .append("<!DOCTYPE html>\n")
            .append("<html lang=\"pt-BR\">\n")
            .append(generateHead())
            .append(generateBody(contents))
            .append("</html>");

        printWriter.write(builder.toString());
    }

    private String generateHead() {
        return 
        """
        <head>
            <meta charset='utf-8'>
            <meta http-equiv='X-UA-Compatible' content='IE=edge'>
            <title>Top 250 movies</title>
            <meta name='viewport' content='width=device-width, initial-scale=1'>
            <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">
            %s        
        </head>
        """.formatted(generateStyles());
    }

    private String generateBody(List<? extends Content> contents) {
        return 
        """
        <body class=\"bg-secondary\">
            %s
            <div class=\"container\">
            %s
            </div>
            <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>
        </body>        
        """.formatted(generateHeader("Marvel Series"), generateCards(contents));
    }

    private String generateCards(List<? extends Content> contents) {
        return  contents.stream()
            .map(serie ->  
                """
                    <div class=\"card\" style=\"width: 18rem;\">
                    <h5 class=\"card-title\">%s - %s</h5>
                    <h6>%s</h6>
                        <img src=\"%s\" class=\"card-img-top\" alt=\"%s\" loading=\"lazy\">
                        <div class=\"card-body\">
                            <span class=\"badge bg-secondary\">%s</span>
                        </div>
                    </div>
                """.formatted(serie.title(), serie.year(), serie.type(), serie.urlImage(), serie.title(), serie.rating())
            )
            .reduce("", (card1, card2) -> card1 += card2);
    }

    private String generateStyles() {
        return 
        """
        <style>
            header {
                min-height: 6rem;
                padding: 50px;
                background-color: #333;
                display: flex;
                flex-direction: row;
                justify-content: space-between;
                position: sticky;
                top: 0;
                z-index: 1000;
            }
    
            header span {
                font-weight: 600;
                font-size: 1.7em;
                color: #ff9800 !important;
            }
    
            .container {
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
            }
    
            .container div span {
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 1.5em;
                color: #ff9800;
            }
    
            .container .card {
                margin-top: 3rem;
                background-color: #333;
            }
    
            .container .card h5 {
                font-weight: 500;
                padding: 10px;
                background-color: #333;
                color: #ff9800;
            }

            .container .card h6 {
                padding: 0 0 0 5px;
                color: #D7D7D7;
            }
    
            .container .card .card-body {
                background-color: #333;
            }
        </style>
        """;
    }

    private String generateHeader(String api) {
        return 
        """
        <header>
            <span>7 Days of Code</span><span>%s</span>
        </header>
        """.formatted(api);
    }

}
