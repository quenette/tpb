/*
 * Copyright (c) 2011-2013, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package edu.monash.merc.dao;

import edu.monash.merc.common.name.DataType;
import edu.monash.merc.domain.PTMEvidence;
import edu.monash.merc.repository.IPTMEvidenceRep;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 17/06/13 12:23 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class PTMEvidenceDAO extends HibernateGenericDAO<PTMEvidence> implements IPTMEvidenceRep {

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePTMEvidenceById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS ptev WHERE ptev.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PTMEvidence getPTMSummaryByGeneAndType(long geneId, DataType dataType) {
        Criteria ptmEvCriteria = this.session().createCriteria(this.persistClass);
        Criteria gCriteria = ptmEvCriteria.createCriteria("gene");
        Criteria dtypeCriteria = ptmEvCriteria.createCriteria("tpbDataType");

        gCriteria.add(Restrictions.eq("id", geneId));

        Disjunction dtypeOr = Restrictions.disjunction();
        //PTM
        if (dataType.equals(DataType.PTM)) {
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_SER.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_TYR.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_THR.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_LYS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_NTA.type()));
            dtypeCriteria.add(dtypeOr);
        } else if (dataType.equals(DataType.PTM_PHS)) {
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_SER.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_TYR.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_THR.type()));
            dtypeCriteria.add(dtypeOr);
        } else if (dataType.equals(DataType.PTM_PHS_SER)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_PHS_SER.type()));
        } else if (dataType.equals(DataType.PTM_PHS_TYR)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_PHS_TYR.type()));
        } else if (dataType.equals(DataType.PTM_PHS_THR)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_PHS_THR.type()));
        } else if (dataType.equals(DataType.PTM_ACE)) {
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_LYS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_NTA.type()));
            dtypeCriteria.add(dtypeOr);
        } else if (dataType.equals(DataType.PTM_ACE_LYS)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_ACE_LYS.type()));
        } else if (dataType.equals(DataType.PTM_ACE_NTA)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_ACE_NTA.type()));
        } else {
            return null;
        }
        ptmEvCriteria.addOrder(Order.desc("colorLevel"));
        ptmEvCriteria.setMaxResults(1);
        return (PTMEvidence) ptmEvCriteria.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PTMEvidence> getPTMEvidencesByGeneAndType(long geneId, DataType dataType) {
        Criteria ptmEvCriteria = this.session().createCriteria(this.persistClass);
        Criteria gCriteria = ptmEvCriteria.createCriteria("gene");
        Criteria dtypeCriteria = ptmEvCriteria.createCriteria("tpbDataType");
        Criteria acCriteria = ptmEvCriteria.createCriteria("identifiedAccession");
        acCriteria.addOrder(Order.asc("accession"));

        gCriteria.add(Restrictions.eq("id", geneId));

        Disjunction dtypeOr = Restrictions.disjunction();
        //PTM
        if (dataType.equals(DataType.PTM)) {
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_SER.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_TYR.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_THR.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_LYS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_NTA.type()));
            dtypeCriteria.add(dtypeOr);
        } else if (dataType.equals(DataType.PTM_PHS)) {
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_SER.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_TYR.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_PHS_THR.type()));
            dtypeCriteria.add(dtypeOr);
        } else if (dataType.equals(DataType.PTM_PHS_SER)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_PHS_SER.type()));
        } else if (dataType.equals(DataType.PTM_PHS_TYR)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_PHS_TYR.type()));
        } else if (dataType.equals(DataType.PTM_PHS_THR)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_PHS_THR.type()));
        } else if (dataType.equals(DataType.PTM_ACE)) {
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_LYS.type()));
            dtypeOr.add(Restrictions.eq("dataType", DataType.PTM_ACE_NTA.type()));
            dtypeCriteria.add(dtypeOr);
        } else if (dataType.equals(DataType.PTM_ACE_LYS)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_ACE_LYS.type()));
        } else if (dataType.equals(DataType.PTM_ACE_NTA)) {
            dtypeCriteria.add(Restrictions.eq("dataType", DataType.PTM_ACE_NTA.type()));
        } else {
            //nothing
        }

        ptmEvCriteria.addOrder(Order.desc("colorLevel"));
        return ptmEvCriteria.list();
    }
}
