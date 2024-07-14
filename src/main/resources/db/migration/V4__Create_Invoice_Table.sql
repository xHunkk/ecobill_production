CREATE TABLE Invoice (
    id BIGSERIAL PRIMARY KEY,
    qr_code BIGINT UNIQUE,
    company_commercial_register BIGINT,
    company_tax_number BIGINT,
    customer_phone_number BIGINT,
    creation_date DATE,
    total_amount DOUBLE PRECISION,
    vat_amount DOUBLE PRECISION,
    total_amount_with_vat DOUBLE PRECISION,
    payment_method VARCHAR(255),
    CONSTRAINT fk_epr_commercial_register FOREIGN KEY (company_commercial_register) REFERENCES EPR(commercial_register),
    CONSTRAINT fk_epr_tax_number FOREIGN KEY (company_tax_number) REFERENCES EPR(tax_number),
    CONSTRAINT fk_customer_phone_number FOREIGN KEY (customer_phone_number) REFERENCES Customer(phone_number)
);
