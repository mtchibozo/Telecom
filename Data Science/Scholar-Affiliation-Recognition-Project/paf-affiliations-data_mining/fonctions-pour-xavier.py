##Fonctions Elementaires
import csv
from urllib2 import Request, urlopen
import PyPDF2
import shutil

path_to_gridfolder = "grid20180501\\"

def getAddressFromUniversityId(id):
    with open('addresses.csv', 'r') as f:
        reader = csv.reader(f)
        for row in reader:
            if id in row:
                return(row[8],row[11],"lat : ",row[4]," long : ", row[5])
    return -1
   
   # row[8]:ville
   # row[11]: pays

def getNonAffiliationLinesFromFile(file, aff_set):          #Renvoie une liste de string
    text=pdfToTextFromFile(file)
    def para_sep(string):
        L = string.split('\n')
        for ph in L : 
            ph+='\n'
        return L
    textList=para_sep(text)
    nonAffiliationTextList=[]
    affiliation=False       #booleen permettant de verifier si une des affiliations est contenue dans la ligne en cours de lecture
    for line in textList:
        for affiliation in aff_set:
            if isAffiliationInText(affiliation,line)==True:
                affiliation=True
        if affiliation==False:
            nonAffiliationTextList.append(line)
        affiliation=False  
    return nonAffiliationTextList

def getNonAffiliationLinesFromUrl(url,aff_set):             #Renvoie une liste de string
    file="temppdf.txt"
    local_directory=os.getcwd()
    output_file = local_directory +"//output.pdf"
    with urlopen(url) as response, open('output.pdf', 'wb') as out_file:
        shutil.copyfileobj(response, out_file)
    return getNonAffiliationLinesFromFile(output_file,aff_set)
    
# def getNonAffiliationLines(text, aff_set):
#     f=open('pdf.txt','w')
#     fichier = open('nonaffiliationlines.txt','a')
#     pdfl = getPDFContent(url).encode("ascii", "ignore")
#     f.write(pdfl)   #contient une copie du pdf mais en .txt
    # affiliation=False       #booleen permettant de verifier si une des affiliations est contenue dans la ligne en cours de lecture
    # for line in f.readlines():
    #     for affiliation in aff_set:
    #         if isAffiliationInText(affiliation,line)==True:
    #             affiliation=True
    #     if affiliation==False:
    #         fichier.write(line)
    #     affiliation=False          
#     fichier.close()
#     f.close()


def pdfToTextFromFile(filename):
    pdfFileObj = open(filename,'rb')
    pdfReader = PyPDF2.PdfFileReader(pdfFileObj)
    num_pages = pdfReader.numPages
    count = 0
    text = ""
    while count < num_pages:
        pageObj = pdfReader.getPage(count)
        count +=1
        text += pageObj.extractText()
    return text



def isAffiliationInText(affiliation,line):
    if affiliation in line: 
        return True
    else:
        return False


    
                
