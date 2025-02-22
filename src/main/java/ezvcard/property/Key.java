package ezvcard.property;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.ValidationWarning;
import ezvcard.parameter.KeyType;

/*
 Copyright (c) 2012-2023, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * <p>
 * Defines a public encryption key.
 * </p>
 * 
 * <p>
 * <b>Code sample (creating)</b>
 * </p>
 * 
 * <pre class="brush:java">
 * VCard vcard = new VCard();
 * 
 * //URL
 * Key key = new Key("http://www.mywebsite.com/my-public-key.pgp", KeyType.PGP);
 * vcard.addKey(key);
 * 
 * //URI
 * Key key = new Key("OPENPGP4FPR:ABAF11C65A2970B130ABE3C479BE3E4300411886", null);
 * vcard.addKey(key);
 * 
 * //binary data
 * byte data[] = ...
 * key = new Key(data, KeyType.PGP);
 * vcard.addKey(key);
 * 
 * //plain text value
 * key = new Key();
 * key.setText("...", KeyType.PGP);
 * vcard.addKey(key);
 * </pre>
 * 
 * <p>
 * <b>Code sample (retrieving)</b>
 * </p>
 * 
 * <pre class="brush:java">
 * VCard vcard = ...
 * for (Key key : vcard.getKeys()) {
 *   KeyType contentType = key.getContentType(); //e.g. "application/pgp-keys"
 * 
 *   String url = key.getUrl();
 *   if (url != null) {
 *     //property value is a URL
 *     continue;
 *   }
 *   
 *   byte[] data = key.getData();
 *   if (data != null) {
 *     //property value is binary data
 *     continue;
 *   }
 *   
 *   String text = key.getText();
 *   if (text != null) {
 *     //property value is plain-text
 *     continue;
 *   }
 * }
 * </pre>
 * 
 * <p>
 * <b>Property name:</b> {@code KEY}
 * </p>
 * <p>
 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
 * </p>
 * @author Michael Angstadt
 * @see <a href="http://tools.ietf.org/html/rfc6350#page-48">RFC 6350 p.48</a>
 * @see <a href="http://tools.ietf.org/html/rfc2426#page-26">RFC 2426 p.26</a>
 * @see <a href="http://www.imc.org/pdi/vcard-21.doc">vCard 2.1 p.22</a>
 */
public class Key extends BinaryProperty<KeyType> {
	private String text;

	/**
	 * Creates an empty key property.
	 */
	public Key() {
		super();
	}

	/**
	 * Creates a key property.
	 * @param data the binary data
	 * @param type the type of key (e.g. PGP)
	 */
	public Key(byte data[], KeyType type) {
		super(data, type);
	}

	/**
	 * Creates a key property.
	 * @param url the URI or URL to the key
	 * @param type the type of key (e.g. PGP) or null if the type can be
	 * identified from the URI
	 */
	public Key(String url, KeyType type) {
		super(url, type);
	}

	/**
	 * Creates a key property.
	 * @param in an input stream to the binary data (will be closed)
	 * @param type the content type (e.g. PGP)
	 * @throws IOException if there's a problem reading from the input stream
	 */
	public Key(InputStream in, KeyType type) throws IOException {
		super(in, type);
	}

	/**
	 * Creates a key property.
	 * @param file the key file
	 * @param type the content type (e.g. PGP)
	 * @throws IOException if there's a problem reading from the file
	 */
	public Key(Path file, KeyType type) throws IOException {
		super(file, type);
	}

	/**
	 * Copy constructor.
	 * @param original the property to make a copy of
	 */
	public Key(Key original) {
		super(original);
		text = original.text;
	}

	/**
	 * Sets a plain text representation of the key.
	 * @param text the key in plain text
	 * @param type the key type
	 */
	public void setText(String text, KeyType type) {
		this.text = text;
		data = null;
		url = null;
		setContentType(type);
	}

	/**
	 * Gets the plain text representation of the key.
	 * @return the key in plain text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the URL or URI of the key.
	 * @param url the URL or URI of the key
	 * @param type the type of key (e.g. PGP) or null if the type can be
	 * identified from the URI
	 */
	@Override
	public void setUrl(String url, KeyType type) {
		super.setUrl(url, type);
		text = null;
	}

	/**
	 * Gets the URL or URI of the key.
	 * @return the URL/URI or null if there is none
	 */
	@Override
	public String getUrl() {
		return super.getUrl();
	}

	@Override
	public void setData(byte[] data, KeyType type) {
		super.setData(data, type);
		text = null;
	}

	@Override
	protected void _validate(List<ValidationWarning> warnings, VCardVersion version, VCard vcard) {
		if (url == null && data == null && text == null) {
			warnings.add(new ValidationWarning(8));
		}

		if (url != null && (version == VCardVersion.V2_1 || version == VCardVersion.V3_0)) {
			warnings.add(new ValidationWarning(15));
		}
	}

	@Override
	protected Map<String, Object> toStringValues() {
		Map<String, Object> values = super.toStringValues();
		values.put("text", text);
		return values;
	}

	@Override
	public Key copy() {
		return new Key(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		Key other = (Key) obj;
		if (text == null) {
			if (other.text != null) return false;
		} else if (!text.equals(other.text)) return false;
		return true;
	}
}
