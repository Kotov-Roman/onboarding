<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <body>
        <!--  <h2>My CD Collection</h2>-->
        <!--    <xsl:value-of select="root//catalog//cd/title[1]"></xsl:value-of>-->
        <br/>

        <xsl:for-each select="//DIVISION//TITLE">
          <xsl:value-of select="./TI[1]/P"/>  <xsl:value-of select="./STI"/>
          <br/>
        </xsl:for-each>
        <!--    <xsl:for-each select="root/catalog//cd">-->
        <!--      <p>TEXT:  <xsl:value-of select="title"/>-->
        <!--        <br/>-->
        <!--    <xsl:value-of select="artist"/>-->
        <!--      </p>-->
        <!--    </xsl:for-each>-->
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>