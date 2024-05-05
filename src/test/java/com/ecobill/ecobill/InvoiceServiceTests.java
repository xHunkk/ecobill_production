// package com.ecobill.ecobill.Services.impl;

// import com.ecobill.ecobill.domain.dto.InvoiceDto;
// import com.ecobill.ecobill.domain.entities.EPREntity;
// import com.ecobill.ecobill.domain.entities.InvoiceEntity;
// import com.ecobill.ecobill.mappers.impl.InvoiceMapperImpl;
// import com.ecobill.ecobill.repositories.InvoiceRepository;
// import com.ecobill.ecobill.services.impl.EPRServiceImpl;
// import com.ecobill.ecobill.services.impl.InvoiceServiceImpl;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;

// class InvoiceServiceTests {
// @InjectMocks
// private InvoiceServiceImpl invoiceService;
// @Mock
// private InvoiceRepository repository;
// @Mock
// private InvoiceMapperImpl invoiceMapper;
// @Mock
// private EPRServiceImpl eprService;
// @Mock
// private InvoiceServiceImpl invoiceServiceMock;

// private static InvoiceEntity invoice;
// private static List<InvoiceEntity> invoices;
// private static List<InvoiceDto> invoicesDto;
// private static EPREntity epr;
// private static List<EPREntity> eprs;

// @BeforeAll
// public static void setUp() {
// invoice = new InvoiceEntity();
// invoice.setId(888L);
// epr = new EPREntity();
// epr.setId(888L);
// epr.setName("");
// InvoiceEntity tempInvoice;
// InvoiceDto tempInvoiceDto;
// invoices = new ArrayList<>();
// invoicesDto = new ArrayList<>();
// for (Long i = 1L; i <= 10L; i++) {
// tempInvoice = new InvoiceEntity();
// tempInvoiceDto = new InvoiceDto();
// tempInvoiceDto.setId(i);
// tempInvoice.setId(i);
// invoices.add(tempInvoice);
// invoicesDto.add(tempInvoiceDto);
// }
// EPREntity temp2;
// eprs = new ArrayList<>();
// for (Long i = 1L; i <= 5L; i++) {
// temp2 = new EPREntity();
// temp2.setId(i);
// temp2.setName("");
// eprs.add(temp2);
// }

// }

// @BeforeEach
// public void setup() {
// MockitoAnnotations.openMocks(this);
// }

// @Test
// public void testFindByEPR() {
// Mockito.when(eprService.getEPRByName("")).thenReturn(epr);
// Mockito.when(repository.findAllByEpr(epr)).thenReturn(invoices);
// Mockito.when(invoiceMapper.mapTo(any(InvoiceEntity.class))).thenReturn(new
// InvoiceDto());
// List<InvoiceDto> response = invoiceService.getByEPR("");
// assertNotNull(response);
// assertEquals(invoices.size(), response.size());
// Mockito.verify(invoiceMapper,
// Mockito.times(invoices.size())).mapTo(any(InvoiceEntity.class));
// }

// @Test
// public void testFindByEPR_when_EPR_is_null() {
// Mockito.when(repository.findAllByEpr(null)).thenReturn(null);
// List<InvoiceDto> response = invoiceService.getByEPR("");
// assertNull(response);
// }

// @Test
// public void testFindByEPRCategory() {
// Mockito.when(eprService.getByCategory("")).thenReturn(eprs);
// Mockito.when(eprService.getEPRByName("")).thenReturn(epr);
// Mockito.when(repository.findAllByEpr(any(EPREntity.class))).thenReturn(invoices);
// Mockito.when(invoiceMapper.mapTo(any(InvoiceEntity.class))).thenReturn(new
// InvoiceDto());
// List<InvoiceDto> response = invoiceService.getByEPRCategory("");
// assertNotNull(response);
// assertEquals(invoices.size() * 5, response.size());
// }

// @Test
// public void testFindByEPRCategory_when_eprs_is_empty() {
// Mockito.when(eprService.getByCategory("")).thenReturn(new ArrayList<>());
// Mockito.when(eprService.getEPRByName("")).thenReturn(epr);
// Mockito.when(repository.findAllByEpr(any(EPREntity.class))).thenReturn(invoices);
// Mockito.when(invoiceMapper.mapTo(any(InvoiceEntity.class))).thenReturn(new
// InvoiceDto());
// List<InvoiceDto> response = invoiceService.getByEPRCategory("");
// assertEquals(0, response.size());
// }

// @Test
// public void testFindByEPRCategory_when_Invoices_is_empty() {
// Mockito.when(eprService.getByCategory("")).thenReturn(eprs);
// Mockito.when(eprService.getEPRByName("")).thenReturn(epr);
// Mockito.when(repository.findAllByEpr(any(EPREntity.class))).thenReturn(new
// ArrayList<>());
// Mockito.when(invoiceMapper.mapTo(any(InvoiceEntity.class))).thenReturn(new
// InvoiceDto());
// List<InvoiceDto> response = invoiceService.getByEPRCategory("");
// assertEquals(0, response.size());
// }

// }