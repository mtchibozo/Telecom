#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Propose un ensemble de fonctions pour faciliter l'interaction avec les objets de pdfminer
"""

from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
from pdfminer.pdfpage import PDFPage
from pdfminer.pdfpage import PDFTextExtractionNotAllowed
from pdfminer.pdfinterp import PDFResourceManager
from pdfminer.pdfinterp import PDFPageInterpreter
from pdfminer.pdfdevice import PDFDevice

from pdfminer.layout import LAParams
from pdfminer.converter import PDFPageAggregator


def parse_document(pdf_file):
    parser = PDFParser(pdf_file)
    document = PDFDocument(parser)
    if not document.is_extractable:
        raise PDFTextExtractionNotAllowed
    return document

def create_interpreter():
    rsrcmgr = PDFResourceManager()
    device = PDFDevice(rsrcmgr)
    interpreter = PDFPageInterpreter(rsrcmgr, device)
    return interpreter
    
def create_layout_interpreter():
    laparams = LAParams()
    rsrcmgr = PDFResourceManager()
    device = PDFPageAggregator(rsrcmgr, laparams=laparams)
    interpreter = PDFPageInterpreter(rsrcmgr, device)
    return interpreter
    
def pages_iterator(document):
    return PDFPage.create_pages(document)

def page_layout_generator(interpreter):
    def get_page_layout(page):
        interpreter.process_page(page)
        device = interpreter.device
        return device.get_result()
    return get_page_layout
                


def analyse_layout(document, interpreter):
    device=interpreter.device
    text = ""
    for page in pages_iterator(document):
        interpreter.process_page(page)
        layout = device.get_result()
        for item in layout:
            try:
                text+=item.get_text()+" "
            except:
                continue
    return text


        

