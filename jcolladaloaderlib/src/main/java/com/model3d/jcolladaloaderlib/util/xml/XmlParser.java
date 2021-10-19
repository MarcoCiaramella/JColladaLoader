/*
MIT License

Copyright (c) 2020 The 3Deers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.model3d.jcolladaloaderlib.util.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by andres on 9/12/17.
 */
public class XmlParser {


	public static XmlNode parse(InputStream in)  {
		try {
			XmlPullParser xpp = Xml.newPullParser();
			xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			xpp.setInput(in, null);
			int eventType = xpp.getEventType();
			if (eventType == XmlPullParser.START_DOCUMENT) {
				XmlNode parent = new XmlNode("xml");
				loadNode(xpp, parent);
				return parent.getChild("COLLADA");
			}
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	private static void loadNode(XmlPullParser xpp, XmlNode parentNode) throws XmlPullParserException, IOException {
		int eventType = xpp.next();
		while(eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				XmlNode childNode = new XmlNode(xpp.getName());
				for (int i=0; i<xpp.getAttributeCount(); i++){
					childNode.addAttribute(xpp.getAttributeName(i), xpp.getAttributeValue(i));
				}
				parentNode.addChild(childNode);
				loadNode(xpp, childNode);
			} else if (eventType == XmlPullParser.END_TAG) {
				return;
			} else if (eventType == XmlPullParser.TEXT) {
				parentNode.setData(xpp.getText());
			}
			eventType = xpp.next();
		}
	}
}
