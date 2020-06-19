package ModelC4;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.PaperSize;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

public class Program {
    public Program() {
    }

    public static void main(String[] args) {
        Bank_Java();
    }

    private static void Bank_Java() {
        long workspaceId = 55705;
        String apiKey = "ddd68fb8-5aaa-4842-bf07-c63260645801";
        String apiSecret = "4d4a2536-de4e-4148-b614-4e75881ba4d7";
        
        StructurizrClient structurizrClient = new StructurizrClient(apiKey, apiSecret);
        Workspace workspace = new Workspace("Bank_Java", "Bank_Java - C4 Model");
        Model model = workspace.getModel();
        
        SoftwareSystem internetBankingSystem = model.addSoftwareSystem("Internet Banking", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.");
        SoftwareSystem mainframeBankingSystem = model.addSoftwareSystem("Mainframe Banking", "Almacena información del core bancario.");
        SoftwareSystem mobileAppSystem = model.addSoftwareSystem("Mobile App", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.");
        SoftwareSystem emailSystem = model.addSoftwareSystem("SendGrid", "Servicio de envío de notificaciones por email.");
        
        Person cliente = model.addPerson("Cliente", "Cliente del banco.");
        Person cajero = model.addPerson("Cajero", "Empleado del banco.");
        
        mainframeBankingSystem.addTags("Mainframe");
        mobileAppSystem.addTags("Mobile App");
        emailSystem.addTags("SendGrid");
        
        cliente.uses(internetBankingSystem, "Realiza consultas y operaciones bancarias.");
        cliente.uses(mobileAppSystem, "Realiza consultas y operaciones bancarias.");
        cajero.uses(mainframeBankingSystem, "Usa");
        
        internetBankingSystem.uses(mainframeBankingSystem, "Usa");
        internetBankingSystem.uses(emailSystem, "Envía notificaciones de email");
        mobileAppSystem.uses(internetBankingSystem, "Usa");
        emailSystem.delivers(cliente, "Envía notificaciones de email", "SendGrid");
        ViewSet viewSet = workspace.getViews();
        
        // 1. Diagrama de Contexto
        SystemContextView contextView = viewSet.createSystemContextView(internetBankingSystem, "Contexto", "Diagrama de contexto - Banking");
        contextView.setPaperSize(PaperSize.A4_Landscape);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.enableAutomaticLayout();
        
        Styles styles = viewSet.getConfiguration().getStyles();
        styles.add(new ElementStyle(Tags.PERSON,null,null,"#0a60ff","#ffffff",null,Shape.Person));
        styles.add(new ElementStyle("Mobile App",null,null,"#29c732","#ffffff",null,Shape.MobileDevicePortrait));
        styles.add(new ElementStyle("Mainframe",null,null,"#90714c","#ffffff", null,Shape.RoundedBox));
        styles.add(new ElementStyle("SendGrid",null,null,"#a5cdff","#ffffff",null,Shape.RoundedBox));
        /*
        styles.add(new ElementStyle(Tags.PERSON) { background = "#0a60ff", Color = "#ffffff", Shape = Shape.Person });
        styles.add(new ElementStyle("Mobile App") { background = "#29c732", Color = "#ffffff", Shape = Shape.MobileDevicePortrait });
        styles.add(new ElementStyle("Mainframe") { background = "#90714c", Color = "#ffffff", Shape = Shape.RoundedBox });
        styles.add(new ElementStyle("SendGrid") { background = "#a5cdff", Color = "#ffffff", Shape = Shape.RoundedBox });
        */
        // 2. Diagrama de Contenedores
        Container webApplication = internetBankingSystem.addContainer("Aplicación Web", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.", "ReactJS, nginx port 80");
        Container restApi = internetBankingSystem.addContainer("RESTful API", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.", "Net Core, nginx port 80");
        Container worker = internetBankingSystem.addContainer("Worker", "Manejador del bus de mensajes.", "Net Core");
        Container database = internetBankingSystem.addContainer("Base de Datos", "Repositorio de información bancaria.", "Oracle 12c port 1521");
        Container messageBus = internetBankingSystem.addContainer("Bus de Mensajes", "Transporte de eventos del dominio.", "RabbitMQ");

        webApplication.addTags("WebApp");
        restApi.addTags("API");
        worker.addTags("Worker");
        database.addTags("Database");
        messageBus.addTags("MessageBus");
        
        cliente.uses(webApplication, "Usa", "https 443");
        webApplication.uses(restApi, "Usa", "https 443");
        worker.uses(restApi, "Usa", "https 443");
        worker.uses(messageBus, "Usa");
        worker.uses(mainframeBankingSystem, "Usa");
        restApi.uses(database, "Usa", "jdbc 1521");
        restApi.uses(messageBus, "Usa");
        restApi.uses(emailSystem, "Usa", "https 443");
        mobileAppSystem.uses(restApi, "Usa");
        
        ElementStyle e1 = new ElementStyle("WebApp",null,null,"#9d33d6","#ffffff",null,Shape.WebBrowser);
        e1.setIcon("data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9Ii0xMS41IC0xMC4yMzE3NCAyMyAyMC40NjM0OCI+CiAgPHRpdGxlPlJlYWN0IExvZ288L3RpdGxlPgogIDxjaXJjbGUgY3g9IjAiIGN5PSIwIiByPSIyLjA1IiBmaWxsPSIjNjFkYWZiIi8+CiAgPGcgc3Ryb2tlPSIjNjFkYWZiIiBzdHJva2Utd2lkdGg9IjEiIGZpbGw9Im5vbmUiPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIi8+CiAgICA8ZWxsaXBzZSByeD0iMTEiIHJ5PSI0LjIiIHRyYW5zZm9ybT0icm90YXRlKDYwKSIvPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIiB0cmFuc2Zvcm09InJvdGF0ZSgxMjApIi8+CiAgPC9nPgo8L3N2Zz4K");
        styles.add(e1);
        ElementStyle e2 = new ElementStyle("API",null,null,"#929000","#ffffff",null,Shape.RoundedBox);
        e2.setIcon("https://dotnet.microsoft.com/static/images/redesign/downloads-dot-net-core.svg?v=U_8I9gzFF2Cqi5zUNx-kHJuou_BWNurkhN_kSm3mCmo");
        styles.add(e2);
        ElementStyle e3 = new ElementStyle("Worker",null,null,null,null,null,null);
        e3.setIcon("https://dotnet.microsoft.com/static/images/redesign/downloads-dot-net-core.svg?v=U_8I9gzFF2Cqi5zUNx-kHJuou_BWNurkhN_kSm3mCmo");
        styles.add(e3);
        ElementStyle e4 = new ElementStyle("Database",null,null,"#ff0000","#ffffff",null,Shape.Cylinder);
        e4.setIcon("https://4.bp.blogspot.com/-5JVtZBLlouA/V2LhWdrafHI/AAAAAAAADeU/_3bo_QH1WGApGAl-U8RkrFzHjdH6ryMoQCLcB/s200/12cdb.png");
        styles.add(e4);
        ElementStyle e5 = new ElementStyle("MessageBus",850,null,"#fd8208","#ffffff",null,Shape.Pipe);
        e5.setIcon("https://www.rabbitmq.com/img/RabbitMQ-logo.svg");
        styles.add(e5);
        /*
        styles.Add(new ElementStyle("WebApp") { Background = "#9d33d6", Color = "#ffffff", Shape = Shape.WebBrowser, Icon = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9Ii0xMS41IC0xMC4yMzE3NCAyMyAyMC40NjM0OCI+CiAgPHRpdGxlPlJlYWN0IExvZ288L3RpdGxlPgogIDxjaXJjbGUgY3g9IjAiIGN5PSIwIiByPSIyLjA1IiBmaWxsPSIjNjFkYWZiIi8+CiAgPGcgc3Ryb2tlPSIjNjFkYWZiIiBzdHJva2Utd2lkdGg9IjEiIGZpbGw9Im5vbmUiPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIi8+CiAgICA8ZWxsaXBzZSByeD0iMTEiIHJ5PSI0LjIiIHRyYW5zZm9ybT0icm90YXRlKDYwKSIvPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIiB0cmFuc2Zvcm09InJvdGF0ZSgxMjApIi8+CiAgPC9nPgo8L3N2Zz4K" });
        styles.Add(new ElementStyle("API") { Background = "#929000", Color = "#ffffff", Shape = Shape.RoundedBox, Icon = "https://dotnet.microsoft.com/static/images/redesign/downloads-dot-net-core.svg?v=U_8I9gzFF2Cqi5zUNx-kHJuou_BWNurkhN_kSm3mCmo" });
        styles.Add(new ElementStyle("Worker") { Icon = "https://dotnet.microsoft.com/static/images/redesign/downloads-dot-net-core.svg?v=U_8I9gzFF2Cqi5zUNx-kHJuou_BWNurkhN_kSm3mCmo" });
        styles.Add(new ElementStyle("Database") { Background = "#ff0000", Color = "#ffffff", Shape = Shape.Cylinder, Icon = "https://4.bp.blogspot.com/-5JVtZBLlouA/V2LhWdrafHI/AAAAAAAADeU/_3bo_QH1WGApGAl-U8RkrFzHjdH6ryMoQCLcB/s200/12cdb.png" });
        styles.Add(new ElementStyle("MessageBus") { Width = 850, Background = "#fd8208", Color = "#ffffff", Shape = Shape.Pipe, Icon = "https://www.rabbitmq.com/img/RabbitMQ-logo.svg" });
        */
        ContainerView containerView = viewSet.createContainerView(internetBankingSystem, "Contenedor", "Diagrama de contenedores - Banking");
        contextView.setPaperSize(PaperSize.A4_Landscape);
        containerView.addAllElements();
        containerView.enableAutomaticLayout();
        
        // 3. Diagrama de Componentes
        Component transactionController = restApi.addComponent("Transactions Controller", "Allows users to perform transactions.", "Spring Boot REST Controller");
        Component signinController = restApi.addComponent("SignIn Controller", "Allows users to sign in to the Internet Banking System.", "Spring Boot REST Controller");
        Component accountsSummaryController = restApi.addComponent("Accounts Controller", "Provides customers with an summary of their bank accounts.", "Spring Boot REST Controller");
        Component securityComponent = restApi.addComponent("Security Component", "Provides functionality related to signing in, changing passwords, etc.", "Spring Bean");
        Component mainframeBankingSystemFacade = restApi.addComponent("Mainframe Banking System Facade", "A facade onto the mainframe banking system.", "Spring Bean");

        //restApi.Components.Where(c => "Spring Boot REST Controller".Equals(c.Technology)).ToList().ForEach(c => webApplication.Uses(c, "Uses", "HTTPS"));
        restApi.getComponents();
        signinController.uses(securityComponent, "Uses");
        accountsSummaryController.uses(mainframeBankingSystemFacade, "Uses");
        securityComponent.uses(database, "Reads from and writes to", "JDBC");
        mainframeBankingSystemFacade.uses(mainframeBankingSystem, "Uses", "XML/HTTPS");
        
        ComponentView componentViewForRestApi = viewSet.createComponentView(restApi, "Components", "The components diagram for the REST API");
        componentViewForRestApi.setPaperSize(PaperSize.A4_Landscape);
        componentViewForRestApi.addAllContainers();
        componentViewForRestApi.addAllComponents();
        componentViewForRestApi.add(cliente);
        componentViewForRestApi.add(mainframeBankingSystem);

        try {
            structurizrClient.unlockWorkspace(workspaceId);
            structurizrClient.putWorkspace(workspaceId, workspace);
        } catch (StructurizrClientException e) {
            e.printStackTrace();
        }
    }
}
