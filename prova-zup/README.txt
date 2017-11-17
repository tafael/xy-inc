A arquitetura proposta foi a seguinte:
  * Para cada modelo criado pelo desenvolvedor, é salvo os metadados do modelo em duas tabelas do banco de dados, uma com o modelo e outra com os campos pertencentes ao modelo.
  * Para cada metadado inserido é criada uma tabela que corresponde ao modelo inserido.
  * A partir dos metadados e da tabela criada é possível mapear com uma API RESTful as operações básicas CRUD para cada modelo inserido.
  * O sistema utiliza as informações dos metadados tanto para gerar as consultas SQL automaticamente (SELECT, INSERT, UPDATE e DELETE), quanto para mapear as requisições http na API RESTful.
  * Sistema desenvolvido em JAVA.
  * Tecnologias utilizadas: PostgreSQL, WildFly 8 (Java EE 7), Maven, Hibernate, JSF, JAX-RS WS.

Para executar a aplicação (Windows):

- Baixar e instalar o PostgreSQL (a versão que utilizei foi: "postgresql-9.5.3-1-windows-x64"
- Execute o PgAdmin e crie um banco da seguinte forma:
  host: localhost,
  port: 5432,
  name: prova,
  user: postgres,
  password: postgres
  (Caso tenha que alterar o usuario ou a senha, altere tambem o datasource no arquivo standalone.xml do wildfly)

- Execute o script squema.sql para criar as tabelas do sistema.


- Baixar e instalar o eclipse IDE
- Dentro do eclipse instale os plugins do JBOSS TOOLS e do Maven (M2E). Vá em até o marketplace ( Help -> Eclipse Marketplace... ) e pesquise pelos plugins por lá.
- Baixar o Wildfly 8.2.1.Final e extrair os arquivos para C:\wildfly-8.2.1.Final
- Adicionar o driver do PostgreSQL como modulo do Wildfly: Baixe o driver 'postgresql-9.2-1002-jdbc4.jar' e mova-o para a pasta C:\wildfly-8.2.1.Final\modules\system\layers\base\org\postgresql\main (Crie os diretorios que forem necessarios).
  Na pasta com driver do Postgres, crie o arquivo C:\wildfly-8.2.1.Final\modules\system\layers\base\org\postgresql\main\module.xml com o seguinte conteúdo:

		<?xml version="1.0" encoding="UTF-8"?>
		<module xmlns="urn:jboss:module:1.3" name="org.postgresql">
		    <resources>
		        <resource-root path="postgresql-9.2-1002-jdbc4.jar"/>
		        <!-- Make sure this matches the name of the JAR you are installing -->
		    </resources>
		    <dependencies>
		        <module name="javax.api"/>
		        <module name="javax.transaction.api"/>
		        <module name="javax.servlet.api" optional="true"/>
		    </dependencies>
		</module>

- edite o arquivo standalone.xml que fica em  C:\wildfly-8.2.1.Final\standalone\configuration\standalone.xml da seguinte forma:

	Na tag <default-bindings ... , remova a propriedade "datasource" (aqui eu apaguei o DataSource que vem pre-configurado). 

	Na parte <subsystem xmlns="urn:jboss:domain:datasources:2.0"> ... </subsystem> deve ficar:

		<subsystem xmlns="urn:jboss:domain:datasources:2.0">
            <datasources>
                <xa-datasource jndi-name="java:jboss/datasources/LocalDS" pool-name="LocalDS" enabled="true" use-java-context="true">
                    <xa-datasource-property name="ServerName">
                        localhost
                    </xa-datasource-property>
                    <xa-datasource-property name="PortNumber">
                        5432
                    </xa-datasource-property>
                    <xa-datasource-property name="DatabaseName">
                        prova
                    </xa-datasource-property>
                    <driver>postgresql</driver>
                    <xa-pool>
                        <min-pool-size>5</min-pool-size>
                        <initial-pool-size>5</initial-pool-size>
                        <max-pool-size>100</max-pool-size>
                        <prefill>true</prefill>
                    </xa-pool>
                    <security>
                        <user-name>postgres</user-name>
                        <password>postgres</password>
                    </security>
                    <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker"/>
                        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter"/>
                    </validation>
                </xa-datasource>
                <drivers>
                    <driver name="h2" module="com.h2database.h2">
                        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                    </driver>
                    <driver name="postgresql" module="org.postgresql">
                        <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
                        <datasource-class>org.postgresql.Driver</datasource-class>
                    </driver>
                </drivers>
            </datasources>
        </subsystem>
        
- No Eclipse adicione a View Servers (Window -> Show View -> Servers )
- Adicione um novo servidor WildFly 8.x e aponte-o para o Wildfly 8.2.1 que acabou de baixar.
- Faça o clone do projeto pelo github https://github.com/tafael/xy-inc/tree/master/prova-zup
- Importe o projeto no Eclipse (File -> Import -> Existing Maven Projects) selecione o pom.xml do projeto.
- Execute o goal maven install no projeto (botao direito encima do projeto -> Debug As -> Maven Install ).
- Na View Servers ( botão direito encima do Wildfly -> Add and Remove... -> Add ) adicione o projeto ao Wildfly.
- Na View Servers execute o Wildfly pelo eclipse (icone com um inseto, modo debug).
- acessar http://localhost:8080/prova-zup/
- (caso utilize outra porta que não seja 8080, trocar a propriedade "host_url" no arquivo "config.properties" colocando a porta utilizada, pois o sistema depende desta propriedade para acessar a API RESTful internamente. )
