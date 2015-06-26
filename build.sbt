name := "play-java-springldap"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

val springVersion = "4.1.6.RELEASE"
val springLdapVersion = "2.0.3.RELEASE"

libraryDependencies ++= Seq(
  javaCore,
  cache,
  javaWs,
  "org.webjars" % "jquery-ui" % "1.11.4",
  "org.webjars" % "bootstrap" % "3.3.5",
  "commons-pool" % "commons-pool" % "1.6",
  "org.springframework.data" % "spring-data-commons-core" % "1.4.1.RELEASE",
  "org.springframework" % "spring-expression" % springVersion,
  "org.springframework" % "spring-aop" % springVersion,
  "org.springframework" % "spring-test" % springVersion % "test",
  "org.springframework.ldap" % "spring-ldap-core" % springLdapVersion,
  "com.unboundid" % "unboundid-ldapsdk" % "3.0.0" % "test"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
// routesGenerator := InjectedRoutesGenerator
