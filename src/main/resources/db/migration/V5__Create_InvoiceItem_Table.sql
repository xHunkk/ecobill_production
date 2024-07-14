CREATE TABLE InvoiceItem (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    price DOUBLE PRECISION,
    quantity INT,
    invoice_id BIGINT,
    CONSTRAINT fk_invoice FOREIGN KEY (invoice_id) REFERENCES Invoice(id)
);
