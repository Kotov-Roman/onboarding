<?xml version="1.0" encoding="UTF-8"?>
<!--<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">-->
<!--  <xsl:template match="/">-->
<!--    <html>-->
<!--      <body>-->
<!--        <h2>My CD Collection</h2>-->
<!--        <table border="1">-->
<!--          <tr bgcolor="#9acd32">-->
<!--            <th style="text-align:left">Title</th>-->
<!--            <th style="text-align:left">Artist</th>-->
<!--          </tr>-->
<!--          <xsl:for-each select="catalog/cd">-->
<!--            <tr>-->
<!--              <td><xsl:value-of select="title"/></td>-->
<!--              <td><xsl:value-of select="artist"/></td>-->
<!--            </tr>-->
<!--          </xsl:for-each>-->
<!--        </table>-->
<!--      </body>-->
<!--    </html>-->
<!--  </xsl:template>-->
<!--</xsl:stylesheet>-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
  <html>
  <body>
<!--  <h2>My CD Collection</h2>-->
<!--    <xsl:value-of select="root//catalog//cd/title[1]"></xsl:value-of>-->
    <br/>

<!--    <xsl:for-each select="root//catalog[2]/cd[1]/title">-->
<!--      <p><xsl:value-of select="text()"></xsl:value-of></p>-->
<!--    </xsl:for-each>-->
    <xsl:for-each select="root/catalog//cd">
      <pre><p>TEXT:  <xsl:value-of select="./title"/> <xsl:text> </xsl:text> <xsl:value-of select="artist"/></p></pre>
    </xsl:for-each>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>