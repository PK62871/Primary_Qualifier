Spring Boot @Primary and @Qualifier Explained
##############################################################

This README explains the concepts of @Primary and @Qualifier annotations in Spring Boot, why they are needed, and how Spring resolves bean injection conflicts in depth.

1. The Problem: Bean Injection Conflict
################################################

In Spring, if there are multiple beans of the same type in the application context, Spring cannot decide which one to inject. This results in a runtime exception:

org.springframework.beans.factory.NoUniqueBeanDefinitionException: 
No qualifying bean of type 'com.example.service.MessageService' available: 
expected single matching bean but found 2: emailService,smsService


Here, Spring finds two beans of type MessageService but does not know which one to inject.

2. @Primary Annotation
#################################################

@Primary is used to indicate that one bean should be the default choice when multiple beans of the same type exist.

Example:
public interface MessageService {
    void sendMessage(String message);
}

@Service
@Primary
public class EmailService implements MessageService {
    @Override
    public void sendMessage(String message) {
        System.out.println("Email sent: " + message);
    }
}

@Service
public class SMSService implements MessageService {
    @Override
    public void sendMessage(String message) {
        System.out.println("SMS sent: " + message);
    }
}

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/send")
    public String send() {
        messageService.sendMessage("Hello!");
        return "Message sent!";
    }
}


Explanation:

Both EmailService and SMSService implement MessageService.

@Primary on EmailService tells Spring to inject it when MessageService is required and no qualifier is specified.

Output when calling /api/send:

Email sent: Hello!

3. @Qualifier Annotation

@Qualifier allows explicit selection of which bean to inject by name, even when multiple beans exist.

Example:
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    public MessageController(@Qualifier("smsService") MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/send")
    public String send() {
        messageService.sendMessage("Hello!");
        return "Message sent!";
    }
}


Explanation:

Here, @Qualifier("smsService") explicitly tells Spring to inject the SMSService bean.

Output when calling /api/send:

SMS sent: Hello!

4. How Spring Resolves Bean Injection

Spring uses a resolution process to decide which bean to inject:

By type: Spring first looks for a bean of the required type.

By name: If multiple beans of the same type exist, Spring checks for a bean matching the field or parameter name.

By @Primary: If no name match is found, Spring injects the bean annotated with @Primary.

By @Qualifier: If @Qualifier is specified, Spring overrides the @Primary selection and injects the named bean.

Resolution Flow Diagram:
###############################################
Required Bean Type
       │
       ▼
Multiple Beans Found?
       │
   ┌───┴────────┐
   ▼            ▼
Yes           No Conflict → Inject bean
   │
Check for @Qualifier?
   │
   ▼
Yes → Inject bean with qualifier
No  → Check for @Primary?
        │
        ▼
Yes → Inject @Primary bean
No  → Throw NoUniqueBeanDefinitionException


5. Key Notes

@Primary sets a default bean but can be overridden by @Qualifier.

@Qualifier is safer when multiple beans exist and you want to control injection explicitly.

You can use @Qualifier on constructor, field, or method parameters.

6. Summary Table
#####################################

Annotation	Purpose	When to Use
########################################

@Primary	Marks a default bean among multiple candidates	When one bean should usually be injected

@Qualifier	Specifies a bean explicitly by name	When multiple beans exist and you need control

8. Example with Both
#############################

@Service
@Primary
public class EmailService implements MessageService { ... }

@Service
public class SMSService implements MessageService { ... }

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService defaultService;
    private final MessageService smsService;

    public MessageController(MessageService defaultService,
                             @Qualifier("smsService") MessageService smsService) {
        this.defaultService = defaultService;
        this.smsService = smsService;
    }

    @GetMapping("/sendDefault")
    public String sendDefault() {
        defaultService.sendMessage("Hello Default!");
        return "Default sent!";
    }

    @GetMapping("/sendSms")
    public String sendSms() {
        smsService.sendMessage("Hello SMS!");
        return "SMS sent!";
    }
}


Output:

/sendDefault → Email sent: Hello Default!
/sendSms     → SMS sent: Hello SMS!


This setup ensures that your Spring Boot application resolves bean conflicts cleanly using @Primary and @Qualifier.
